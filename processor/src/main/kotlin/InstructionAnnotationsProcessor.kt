import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import krazyminer001.asmrobots.annotations.ArgumentAnnotation
import krazyminer001.asmrobots.annotations.EnumerateInstructions
import krazyminer001.asmrobots.annotations.Parsable
import krazyminer001.asmrobots.annotations.ParsableEnumerated

class InstructionAnnotationsProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val parsable = resolver.getClassDeclarationByName(Parsable::class.qualifiedName!!)!!

        resolver.getSymbolsWithAnnotation(ParsableEnumerated::class.qualifiedName!!)
            .filter(KSAnnotated::validate)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(ParsableEnumeratedVisitor(parsable, resolver), Unit) }

        resolver.getSymbolsWithAnnotation(EnumerateInstructions::class.qualifiedName!!)
            .filter(KSAnnotated::validate)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(InstructionEnumVisitor(), Unit) }

        return emptyList()
    }

    inner class ParsableEnumeratedVisitor(val parsableDeclaration: KSClassDeclaration, val resolver: Resolver) : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val enumName = ClassName(classDeclaration.qualifiedName!!.getQualifier(), classDeclaration.simpleName.asString() + "Enum")

            val enumBuilder = TypeSpec.enumBuilder(enumName)

            classDeclaration.getSealedSubclasses().forEach { subclass ->
                val constructorParameters = subclass.primaryConstructor!!.parameters
                constructorParameters.forEach { parameter ->
                    val companion = (parameter.type.resolve().declaration as? KSClassDeclaration)
                        ?.declarations
                        ?.filterIsInstance<KSClassDeclaration>()
                        ?.find { it.isCompanionObject }
                    if (companion == null) {
                        logger.error("Constructor parameter types must have companion which implements Parsable<Parameter Type>", parameter)
                        return
                    }

                    if (!parsableDeclaration
                        .asType(listOf(resolver.getTypeArgument(parameter.type, Variance.CONTRAVARIANT)))
                        .isAssignableFrom(companion.asStarProjectedType())) {

                        logger.error("Constructor parameter types must have companion which implements Parsable<Parameter Type>", parameter)
                        return
                    }
                }

                enumBuilder
                    .addEnumConstant(
                        subclass.simpleName.asString(),
                        TypeSpec.anonymousClassBuilder()
                            .addFunction(
                                FunSpec.builder("invoke")
                                    .addModifiers(KModifier.OPERATOR, KModifier.OVERRIDE)
                                    .addParameter("strings", Array::class.asClassName().parameterizedBy(String::class.asClassName()))
                                    .returns(subclass.toClassName())
                                    .addStatement("require(strings.size == %L)", constructorParameters.size)
                                    .addStatement(
                                        if (constructorParameters.isEmpty()) "return %T%L" else "return %T(%L)",
                                        subclass.toClassName(),
                                        constructorParameters.mapIndexed { index, parameter ->
                                            CodeBlock.of("%T.parse(strings[%L])", parameter.type.resolve().toClassName(), index)
                                        }.joinToCode()
                                    )
                                    .build()
                            )
                            .build()
                    )
            }

            enumBuilder
                .addFunction(
                    FunSpec.builder("invoke")
                        .addModifiers(KModifier.OPERATOR, KModifier.ABSTRACT)
                        .returns(classDeclaration.asStarProjectedType().toClassName())
                        .addParameter("strings", Array::class.asClassName().parameterizedBy(String::class.asClassName()))
                        .build()
                )

            FileSpec.builder(enumName).addType(enumBuilder.build()).build().writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }
    }

    inner class InstructionEnumVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val annotation = classDeclaration.annotations.first { it.shortName.asString() == EnumerateInstructions::class.simpleName }
            val otherAnnotationType = annotation.arguments[0].value as KSType
            val argumentTypeAnnotation = otherAnnotationType.declaration.annotations.first { it.shortName.asString() == ArgumentAnnotation::class.simpleName }
            val argumentType = argumentTypeAnnotation.arguments[0].value as KSType

            val enumBuilder = TypeSpec.enumBuilder(classDeclaration.simpleName.asString() + "Enum")
                .addProperty(
                    PropertySpec.builder("types", Array::class.asClassName().parameterizedBy(WildcardTypeName.producerOf(otherAnnotationType.toClassName())))
                        .initializer("types")
                        .build()
                )
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("types", otherAnnotationType.toClassName(), KModifier.VARARG)
                        .build()
                )
                .addFunction(
                    FunSpec.builder("create")
                        .addModifiers(KModifier.ABSTRACT)
                        .returns(classDeclaration.toClassName())
                        .addParameter("arguments", argumentType.toClassName(), KModifier.VARARG)
                        .build()
                )
                .addFunction(
                    FunSpec.builder("toBytes")
                        .returns(ByteArray::class)
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter("value", classDeclaration.toClassName())
                        .build()
                )
                .addFunction(
                    FunSpec.builder("isValid")
                        .returns(Boolean::class)
                        .addParameter("arguments", argumentType.toClassName(), KModifier.VARARG)
                        .addStatement("if (types.size != arguments.size) return false")
                        .addStatement("return arguments.zip(types).all { (argument, type) -> type.validTypes.any { it.isInstance(argument) } }")
                        .build()
                )

            classDeclaration.getSealedSubclasses().forEach { subclass ->
                val constructor = subclass.primaryConstructor!!
                val parameters = constructor.parameters
                if (!parameters.all { it.type.resolve() == argumentType }) {
                    logger.error("Invalid argument type", constructor)
                }

                val annotations = parameters.map { parameter -> parameter.type.annotations.first { it.annotationType.resolve() == otherAnnotationType } }

                enumBuilder
                    .addEnumConstant(
                        subclass.simpleName.asString(),
                        TypeSpec.anonymousClassBuilder()
                            .addSuperclassConstructorParameter(
                                "%L",
                                annotations.joinToCode {
                                    CodeBlock.of("%T(%L)", it.annotationType.toTypeName(), it.toAnnotationSpec().members.joinToCode())
                                }
                            )
                            .addFunction(
                                FunSpec.builder("create")
                                    .returns(classDeclaration.toClassName())
                                    .addModifiers(KModifier.OVERRIDE)
                                    .addParameter("arguments", argumentType.toClassName(), KModifier.VARARG)
                                    .addStatement("require(this.isValid(*arguments))")
                                    .addStatement(if (parameters.isEmpty()) "return %T%L" else "return %T(%L)", subclass.toClassName(), parameters.indices.toList().joinToCode { CodeBlock.of("arguments[%L]", it) })
                                    .build()
                            )
                            .addFunction(
                            FunSpec.builder("toBytes")
                                .returns(ByteArray::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .addParameter("value", classDeclaration.toClassName())
                                .addStatement("require(value is %L)", subclass.toClassName())
                                .apply {
                                    if (!parameters.isEmpty()) {
                                        addStatement("val (%L) = value", parameters.joinToCode { CodeBlock.of("%L", it.name!!.asString()) })
                                        addStatement(
                                            "var num = 0",
                                        )
                                        parameters.forEachIndexed { index, parameter ->
                                            addStatement(
                                                "num += this.types[%L].validTypes.indexOf(%L::class)", index, parameter.name!!.asString()
                                            )
                                            if (index != parameters.size - 1) {
                                                addStatement(
                                                    "num *= this.types[%L].validTypes.size", index
                                                )
                                            }
                                        }
                                    } else {
                                        addStatement(
                                            "val num = 0"
                                        )
                                    }
                                }
                                .addStatement(
                                    "return byteArrayOf(this.ordinal.toUByte().toByte(), num.toUByte().toByte()%L)",
                                    parameters.joinToCode(separator = "") { CodeBlock.of(", *%L.toBytes()", it.name!!.asString()) }
                                )
                                .build()
                        )
                            .build()
                    )
            }

            FileSpec.builder(classDeclaration.packageName.asString(), classDeclaration.simpleName.asString() + "Enum")
                .addType(enumBuilder.build())
                .build()
                .writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return InstructionAnnotationsProcessor(environment.codeGenerator, environment.logger)
        }
    }
}
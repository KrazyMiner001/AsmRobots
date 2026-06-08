import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Variance
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import krazyminer001.asmrobots.annotations.Parsable
import krazyminer001.asmrobots.annotations.ParsableEnumerated

class InstructionAnnotationsProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val parsable = resolver.getClassDeclarationByName(Parsable::class.qualifiedName!!)!!

        resolver.getSymbolsWithAnnotation(ParsableEnumerated::class.qualifiedName!!)
            .filter(KSAnnotated::validate)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(Visitor(parsable, resolver), Unit) }

        return emptyList()
    }

    inner class Visitor(val parsableDeclaration: KSClassDeclaration, val resolver: Resolver) : KSVisitorVoid() {
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

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return InstructionAnnotationsProcessor(environment.codeGenerator, environment.logger)
        }
    }
}
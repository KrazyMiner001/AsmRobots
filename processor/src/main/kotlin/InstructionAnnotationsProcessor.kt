import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import krazyminer001.asmrobots.annotations.ParsableEnumerated

class InstructionAnnotationsProcessor(val codeGenerator: CodeGenerator) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(ParsableEnumerated::class.qualifiedName!!)
            .filter(KSAnnotated::validate)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(Visitor(), Unit) }

        return emptyList()
    }

    inner class Visitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val enumName = ClassName(classDeclaration.qualifiedName!!.getQualifier(), classDeclaration.simpleName.asString() + "Enum")

            val enumBuilder = TypeSpec.enumBuilder(enumName)

            classDeclaration.getSealedSubclasses().forEach { subclass ->
                val constructorParameters = subclass.primaryConstructor!!.parameters

                enumBuilder
                    .addEnumConstant(
                        subclass.simpleName.asString(),
                        TypeSpec.anonymousClassBuilder()
                            .addFunction(
                                FunSpec.builder("invoke")
                                    .addModifiers(KModifier.OPERATOR, KModifier.OVERRIDE)
                                    .addParameter("string", String::class)
                                    .returns(subclass.toClassName())
                                    .addStatement("val split = string.split(%S)", ", ")
                                    .addStatement("require(split.size == %L)", constructorParameters.size)
                                    .addStatement(
                                        "return %T(%L)",
                                        subclass.toClassName(),
                                        constructorParameters.mapIndexed { index, parameter ->
                                            CodeBlock.of("%T.parse(split[%L])", parameter.type.resolve().toClassName(), index)
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
                        .addParameter("string", String::class)
                        .build()
                )

            FileSpec.builder(enumName).addType(enumBuilder.build()).build().writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return InstructionAnnotationsProcessor(environment.codeGenerator)
        }
    }
}
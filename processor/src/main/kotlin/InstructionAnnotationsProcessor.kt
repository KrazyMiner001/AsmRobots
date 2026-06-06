import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
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
            val functionType = Function1::class
                .asClassName()
                .plusParameter(String::class.asClassName())
                .plusParameter(classDeclaration.asStarProjectedType().toClassName())

            val enumName = ClassName(classDeclaration.qualifiedName!!.getQualifier(), classDeclaration.simpleName.asString() + "Enum")

            val enumBuilder = TypeSpec.enumBuilder(enumName)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("parser", functionType)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("parser", functionType, KModifier.PRIVATE)
                        .initializer("parser")
                        .build()
                )

            val companionBuilder = TypeSpec.companionObjectBuilder()

            classDeclaration.getSealedSubclasses().forEach { subclass ->
                val constructorParameters = subclass.primaryConstructor!!.parameters
                val functionName = enumName.member(subclass.simpleName.asString() + "Parser")
                val function = FunSpec.builder(functionName)
                    .addParameter("string", String::class)
                    .returns(subclass.toClassName())
                    .addStatement(
                        "val parsed = string.split(%S).let { %T(%L) }",
                        ", ",
                        subclass.toClassName(),
                        constructorParameters.mapIndexed { index, parameter ->
                            CodeBlock.of("%T.parse(it[%L])", parameter.type.resolve().toClassName(), index)
                        }.joinToCode()
                    )
                    .addStatement("return parsed")
                    .build()

                companionBuilder
                    .addFunction(function)

                enumBuilder
                    .addEnumConstant(
                        subclass.simpleName.asString(),
                        TypeSpec.anonymousClassBuilder()
                            .addSuperclassConstructorParameter("%L", functionName.reference())
                            .build()
                    )
            }

            enumBuilder
                .addFunction(
                    FunSpec.builder("invoke")
                        .addModifiers(KModifier.OPERATOR)
                        .returns(classDeclaration.asStarProjectedType().toClassName())
                        .addParameter("string", String::class)
                        .addStatement("return parser(string)")
                        .build()
                )
                .addType(companionBuilder.build())

            FileSpec.builder(enumName).addType(enumBuilder.build()).build().writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }
    }

    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return InstructionAnnotationsProcessor(environment.codeGenerator)
        }
    }
}
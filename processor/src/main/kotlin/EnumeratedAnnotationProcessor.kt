import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import krazyminer001.asmrobots.annotations.Enumerated
import kotlin.reflect.KFunction

class EnumeratedAnnotationProcessor(val codeGenerator: CodeGenerator) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Enumerated::class.qualifiedName!!)
            .filter(KSAnnotated::validate)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(Visitor(), Unit) }

        return emptyList()
    }

    inner class Visitor : KSVisitorVoid() {
        @OptIn(KspExperimental::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.classKind != ClassKind.INTERFACE) return

            val functionType = KFunction::class
                .asClassName()
                .plusParameter(classDeclaration.asStarProjectedType().toClassName())

            var enumBuilder = TypeSpec.enumBuilder(classDeclaration.simpleName.asString() + "Enum")
                .addProperty(
                    PropertySpec.builder("creator", functionType)
                        .initializer("creator")
                        .build()
                )

            val enumConstructorBuilder = FunSpec.constructorBuilder()
                .addParameter("creator", functionType)

            val annotationTypes = (classDeclaration
                .annotations.first { it.annotationType.resolve().declaration.qualifiedName!!.asString() == Enumerated::class.qualifiedName }
                .arguments.first().value as Collection<*>).map { it as? KSType ?: return }

            annotationTypes
                .forEach {
                    enumConstructorBuilder
                        .addParameter(it.declaration.simpleName.asString(), it.toClassName())

                    enumBuilder.addProperty(
                        PropertySpec.builder(it.declaration.simpleName.asString(), it.toClassName())
                            .initializer(it.declaration.simpleName.asString())
                            .build()
                    )
                }

            enumBuilder.primaryConstructor(enumConstructorBuilder.build())

            classDeclaration.getSealedSubclasses().forEach { subclass ->
                val classBuilder = TypeSpec.anonymousClassBuilder()
                    .addSuperclassConstructorParameter("%L", subclass.toClassName().constructorReference())

                annotationTypes.forEach { annotationType ->
                    val annotation = subclass.annotations.first { annotationType.isAssignableFrom(it.annotationType.resolve()) }

                    val annotationSpec = annotation.toAnnotationSpec()

                    classBuilder
                        .addSuperclassConstructorParameter(
                            "%T(%L)",
                            annotation.annotationType.resolve().toClassName(),
                            annotationSpec.members.joinToCode(separator = ", ")
                        )
                }

                enumBuilder = enumBuilder
                    .addEnumConstant(
                        subclass.simpleName.asString(),
                        classBuilder.build()
                    )
            }

            FileSpec.builder(classDeclaration.packageName.asString(), classDeclaration.simpleName.asString() + "Enum")
                .addType(enumBuilder.build())
                .build().writeTo(codeGenerator, Dependencies(false, classDeclaration.containingFile!!))
        }
    }
}
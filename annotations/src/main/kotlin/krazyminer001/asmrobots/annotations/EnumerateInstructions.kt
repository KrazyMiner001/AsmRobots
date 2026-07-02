package krazyminer001.asmrobots.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class EnumerateInstructions(
    val argumentAnnotationClass: KClass<out Annotation>,
)

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ArgumentAnnotation(val argumentType: KClass<*>)

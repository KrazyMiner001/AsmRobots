package krazyminer001.asmrobots.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Enumerated(@Suppress("unused") vararg val memberAnnotationTypes: KClass<out Annotation>)
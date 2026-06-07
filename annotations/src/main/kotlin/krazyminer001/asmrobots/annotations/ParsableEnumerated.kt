package krazyminer001.asmrobots.annotations

@Target(AnnotationTarget.CLASS)
annotation class ParsableEnumerated
interface Parsable<T> {
    fun parse(string: String): T
}
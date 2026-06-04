package krazyminer001.asmrobots.common.asm

@JvmInline
value class Literal(val value: Int) {
    companion object : AsmParsable<Literal> {
        override fun parse(value: String): Literal? = value.toIntOrNull()?.let(::Literal)
    }
}
package krazyminer001.asmrobots.common.asm

@JvmInline
value class Literal(val value: Int) {
    companion object : Parsable<Literal> {
        override fun parse(string: String): Literal = string.toIntOrNull()?.let(::Literal)
            ?: throw InstructionInvalidParameter("literal", string)
    }
}
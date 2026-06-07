package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.Parsable

@JvmInline
value class Literal(val value: Int) {
    companion object : Parsable<Literal> {
        override fun parse(string: String): Literal = string.toIntOrNull()?.let(::Literal)
            ?: throw InstructionInvalidParameter("literal", string)
    }
}
package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.Parsable

@JvmInline
value class Immediate(val value: Int) {
    companion object : Parsable<Immediate> {
        override fun parse(string: String): Immediate = string.toIntOrNull()?.let(::Immediate)
            ?: throw InstructionInvalidParameter("literal", string)
    }
}
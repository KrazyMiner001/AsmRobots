package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.Parsable

enum class Syscall {
    ;
    companion object : Parsable<Syscall> {
        override fun parse(string: String): Syscall {
            return Syscall.entries.find { it.name.equals(string, true) }
                ?: throw InstructionInvalidParameter("syscall", string)
        }

    }
}
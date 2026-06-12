package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.Parsable

enum class Syscall {
    ;
    companion object : Parsable<Syscall> {
        override fun parse(string: String): Syscall {
            return runCatching { valueOf(string) }.getOrElse { throw IllegalArgumentException(string) }
        }
    }
}
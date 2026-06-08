package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.Parsable

data class Pointer(val register: Register, val offset: Immediate) {
    companion object : Parsable<Pointer> {
        override fun parse(string: String): Pointer {
            return PointerRegex.matchEntire(string)?.let {
                Pointer(
                    Register.parse(it.groups["register"]!!.value),
                    Immediate.parse(it.groups["offset"]!!.value)
                )
            } ?: throw IllegalArgumentException("Could not convert $string into pointer")
        }
    }
}

val PointerRegex: Regex = "(?<offset>-?\\d+?)\\((?<register>\\w+)\\)".toRegex()
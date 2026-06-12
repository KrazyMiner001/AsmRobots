package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.Parsable

data class Label(val name: String) {
    companion object : Parsable<Label> {
        override fun parse(string: String): Label = Label(string)
    }
}

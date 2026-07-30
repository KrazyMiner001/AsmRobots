package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.extension.Extension

interface ProgramCallback {
    fun halt()
    operator fun get(ioAddress: Int): Int
    operator fun set(ioAddress: Int, value: Int)
    operator fun contains(extension: Extension): Boolean
}
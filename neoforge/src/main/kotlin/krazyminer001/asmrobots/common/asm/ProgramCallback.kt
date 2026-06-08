package krazyminer001.asmrobots.common.asm

interface ProgramCallback {
    fun halt()
    operator fun get(ioAddress: Int): Int
    operator fun set(ioAddress: Int, value: Int)
}
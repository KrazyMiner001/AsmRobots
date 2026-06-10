package krazyminer001.asmrobots.common.asm

fun Int.Companion.fromBytes(leastSignificantByte: Byte, byte1: Byte, byte2: Byte, byte3: Byte): Int {
    return leastSignificantByte.toUByte().toInt() or
            (byte1.toUByte().toInt() shl 8) or
            (byte2.toUByte().toInt() shl 16) or
            (byte3.toUByte().toInt() shl 24)
}
package krazyminer001.asmrobots.common.asm

fun Int.Companion.fromBytes(leastSignificantByte: Byte, byte1: Byte, byte2: Byte, byte3: Byte): Int {
    return leastSignificantByte.toUByte().toInt() or
            (byte1.toUByte().toInt() shl 8) or
            (byte2.toUByte().toInt() shl 16) or
            (byte3.toUByte().toInt() shl 24)
}

fun Int.toBytes(): ByteArray {
    return byteArrayOf(
        this.toUByte().toByte(),
        (this ushr 8).toUByte().toByte(),
        (this ushr 16).toUByte().toByte(),
        (this ushr 24).toUByte().toByte()
    )
}
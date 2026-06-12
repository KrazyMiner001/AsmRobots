package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.ArgumentAnnotation
import krazyminer001.asmrobots.annotations.Enumerated
import krazyminer001.asmrobots.common.asm.fromBytes
import krazyminer001.asmrobots.common.asm.toBytes
import kotlin.reflect.KClass
import kotlin.text.get
import krazyminer001.asmrobots.common.asm.instructions.Register as RegisterEnum

@Enumerated(ArgumentData::class)
sealed interface InstructionArgument {
    @ArgumentData(1)
    data class Register(val register: RegisterEnum) : InstructionArgument {
        override fun toBytes(): ByteArray = byteArrayOf(register.ordinal.toUByte().toByte())
    }
    @ArgumentData(4)
    data class Immediate32(val value: Int) : InstructionArgument {
        override fun toBytes(): ByteArray = value.toBytes()
    }
    @ArgumentData(4)
    data class ImmediateFloat32(val value: Float) : InstructionArgument {
        override fun toBytes(): ByteArray = value.toBits().toBytes()
    }
    @ArgumentData(5)
    data class Pointer(val register: RegisterEnum, val offset: Immediate32) : InstructionArgument {
        override fun toBytes(): ByteArray = byteArrayOf(
            register.ordinal.toUByte().toByte(),
            *offset.value.toBytes()
        )
    }
    @ArgumentData(1)
    data class Syscall(val syscall: krazyminer001.asmrobots.common.asm.instructions.Syscall) : InstructionArgument {
        override fun toBytes(): ByteArray = byteArrayOf(syscall.ordinal.toUByte().toByte())
    }

    fun toBytes(): ByteArray

    companion object {
        fun parse(string: String): InstructionArgument? {
            runCatching {
                return Register(RegisterEnum.parse(string))
            }
            runCatching {
                return Immediate32(string.toInt())
            }
            runCatching {
                return ImmediateFloat32(string.toFloat())
            }
            runCatching {
                return PointerRegex.matchEntire(string)?.let {
                    Pointer(
                        RegisterEnum.parse(it.groups["register"]!!.value),
                        Immediate32(it.groups["offset"]!!.value.toInt())
                    )
                }
            }
            runCatching {
                return Syscall(krazyminer001.asmrobots.common.asm.instructions.Syscall.parse(string))
            }
            return null
        }
    }
}

val PointerRegex: Regex = "(?<offset>-?\\d+?)\\((?<register>\\w+)\\)".toRegex()

fun InstructionArgument.Companion.fromBytes(bytes: ByteArray, type: InstructionArgumentEnum): InstructionArgument {
    require(bytes.size == type.ArgumentData.numBytes)
    return when (type) {
        InstructionArgumentEnum.Register -> InstructionArgument.Register(RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!)
        InstructionArgumentEnum.Immediate32 -> InstructionArgument.Immediate32(
            Int.fromBytes(
                bytes[0],
                bytes[1],
                bytes[2],
                bytes[3]
            )
        )
        InstructionArgumentEnum.ImmediateFloat32 -> InstructionArgument.ImmediateFloat32(
            Float.fromBits(Int.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]))
        )
        InstructionArgumentEnum.Pointer -> InstructionArgument.Pointer(
            RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!,
            InstructionArgument.Immediate32(
                Int.fromBytes(bytes[1], bytes[2], bytes[3], bytes[4])
            )
        )
        InstructionArgumentEnum.Syscall -> InstructionArgument.Syscall(Syscall.entries.find { it.ordinal == bytes[0].toInt() }!!)
    }
}

@Target(AnnotationTarget.CLASS)
annotation class ArgumentData(val numBytes: Int)

@Target(AnnotationTarget.TYPE)
@ArgumentAnnotation(InstructionArgument::class)
annotation class ArgumentType(vararg val validTypes: KClass<out InstructionArgument>)
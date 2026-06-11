package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.ArgumentAnnotation
import krazyminer001.asmrobots.annotations.Enumerated
import krazyminer001.asmrobots.common.asm.InstructionArgument.Immediate32
import krazyminer001.asmrobots.common.asm.InstructionArgument.ImmediateFloat32
import krazyminer001.asmrobots.common.asm.InstructionArgument.Pointer
import krazyminer001.asmrobots.common.asm.InstructionArgument.Register
import krazyminer001.asmrobots.common.asm.InstructionArgument.Syscall
import kotlin.reflect.KClass
import krazyminer001.asmrobots.common.asm.InstructionArgument as Argument
import krazyminer001.asmrobots.common.asm.Register as RegisterEnum
import krazyminer001.asmrobots.common.asm.Syscall as SyscallEnum

@Enumerated(ArgumentData::class)
sealed interface InstructionArgument {
    @ArgumentData(1)
    data class Register(val register: RegisterEnum) : Argument {
        override fun toBytes(): ByteArray = byteArrayOf(register.ordinal.toUByte().toByte())
    }
    @ArgumentData(4)
    data class Immediate32(val value: Int) : Argument {
        override fun toBytes(): ByteArray = value.toBytes()
    }
    @ArgumentData(4)
    data class ImmediateFloat32(val value: Float) : Argument {
        override fun toBytes(): ByteArray = value.toBits().toBytes()
    }
    @ArgumentData(5)
    data class Pointer(val register: RegisterEnum, val offset: Immediate32) : Argument {
        override fun toBytes(): ByteArray = byteArrayOf(
            register.ordinal.toUByte().toByte(),
            *offset.value.toBytes()
        )
    }
    @ArgumentData(1)
    data class Syscall(val syscall: SyscallEnum) : Argument  {
        override fun toBytes(): ByteArray = byteArrayOf(syscall.ordinal.toUByte().toByte())
    }

    fun toBytes(): ByteArray

    companion object {
        fun parse(string: String): Argument? {
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
                return Syscall(SyscallEnum.parse(string))
            }
            return null
        }
    }
}

val PointerRegex: Regex = "(?<offset>-?\\d+?)\\((?<register>\\w+)\\)".toRegex()

fun Argument.Companion.fromBytes(bytes: ByteArray, type: InstructionArgumentEnum): Argument {
    require(bytes.size == type.ArgumentData.numBytes)
    return when (type) {
        InstructionArgumentEnum.Register -> Register(RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!)
        InstructionArgumentEnum.Immediate32 -> Immediate32(Int.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]))
        InstructionArgumentEnum.ImmediateFloat32 -> ImmediateFloat32(
            Float.fromBits(Int.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]))
        )
        InstructionArgumentEnum.Pointer -> Pointer(
            RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!,
            Immediate32(
                Int.fromBytes(bytes[1], bytes[2], bytes[3], bytes[4])
            )
        )
        InstructionArgumentEnum.Syscall -> Syscall(SyscallEnum.entries.find { it.ordinal == bytes[0].toInt() }!!)
    }
}

@Target(AnnotationTarget.CLASS)
annotation class ArgumentData(val numBytes: Int)

@Target(AnnotationTarget.TYPE)
@ArgumentAnnotation(Argument::class)
annotation class ArgumentType(vararg val validTypes: KClass<out Argument>)
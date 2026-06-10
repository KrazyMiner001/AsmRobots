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
    data class Register(val register: RegisterEnum) : Argument
    @ArgumentData(4)
    data class Immediate32(val value: Int) : Argument
    @ArgumentData(4)
    data class ImmediateFloat32(val value: Float) : Argument
    @ArgumentData(5)
    data class Pointer(val pointer: krazyminer001.asmrobots.common.asm.Pointer) : Argument
    @ArgumentData(1)
    data class Syscall(val syscall: SyscallEnum) : Argument

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
                return Pointer(krazyminer001.asmrobots.common.asm.Pointer.parse(string))
            }
            runCatching {
                return Syscall(SyscallEnum.parse(string))
            }
            return null
        }
    }
}

fun Argument.Companion.fromBytes(bytes: ByteArray, type: InstructionArgumentEnum): Argument {
    require(bytes.size == type.ArgumentData.numBytes)
    return when (type) {
        InstructionArgumentEnum.Register -> Register(RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!)
        InstructionArgumentEnum.Immediate32 -> Immediate32(Int.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]))
        InstructionArgumentEnum.ImmediateFloat32 -> ImmediateFloat32(
            Float.fromBits(Int.fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]))
        )
        InstructionArgumentEnum.Pointer -> Pointer(Pointer(
            RegisterEnum.entries.find { it.ordinal == bytes[0].toInt() }!!,
            Immediate(
                Int.fromBytes(bytes[1], bytes[2], bytes[3], bytes[4])
            )
        ))
        InstructionArgumentEnum.Syscall -> Syscall(SyscallEnum.entries.find { it.ordinal == bytes[0].toInt() }!!)
    }
}

@Target(AnnotationTarget.CLASS)
annotation class ArgumentData(val numBytes: Int)

@Target(AnnotationTarget.TYPE)
@ArgumentAnnotation(Argument::class)
annotation class ArgumentType(vararg val validTypes: KClass<out Argument>)
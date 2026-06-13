package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.InstructionEnum
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Condition
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Immediate32
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.ImmediateFloat32
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Label
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Register
import kotlin.collections.mapIndexed

@InstructionEnum(ArgumentType::class)
sealed interface InstructionRewrite {
    data class Add(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sub(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mul(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mulh(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Div(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Rem(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sll(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Srl(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sra(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mov(
        val target: @ArgumentType(Register::class, InstructionArgument.Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, InstructionArgument.Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data class Movh(
        val target: @ArgumentType(Register::class, InstructionArgument.Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, InstructionArgument.Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data class Movb(
        val target: @ArgumentType(Register::class, InstructionArgument.Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, InstructionArgument.Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data object Ret : InstructionRewrite
    data object Nop : InstructionRewrite
    data class Push(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : InstructionRewrite
    data class Pop(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : InstructionRewrite
    data class Pushh(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : InstructionRewrite
    data class Poph(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : InstructionRewrite
    data class Pushb(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : InstructionRewrite
    data class Popb(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : InstructionRewrite
    data object Halt : InstructionRewrite
    data class In(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val ioPortAddress: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : InstructionRewrite
    data class Out(
        val ioPortAddress: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : InstructionRewrite
    data class Jump(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument
    ) : InstructionRewrite
    data class And(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Or(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Xor(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : InstructionRewrite
    data class Not(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : InstructionRewrite
    data class JCond(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument,
        val condition: @ArgumentType(Condition::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite

    fun toBytes(): ByteArray {
        return this.asEnum().toBytes(this)
    }

    companion object {
        fun Iterable<InstructionArgumentEnum>.byteLength(): Int = this.sumOf { it.ArgumentData.numBytes }
    }
}

fun InstructionRewrite.asEnum(): InstructionRewriteEnum = InstructionRewriteEnum.entries.find { it.name == this::class.simpleName }!!

fun InstructionRewrite.Companion.identityInstruction(opcode: UByte, typeInformation: UByte): Pair<InstructionRewriteEnum, Array<InstructionArgumentEnum>> {
    val instructionEnum = InstructionRewriteEnum.entries.find { it.ordinal == opcode.toInt() }
    if (instructionEnum == null) throw IllegalArgumentException("Opcode $opcode does not correspond to and instruction")

    val types = instructionEnum.types

    // Checks to make sure current data parsing system works. It most likely will work for all future instructions so these should not ever be reached
    if (types.size > 4) throw NotImplementedError("Instructions with more than 4 parameters are not yet implemented")
    if (types.any { it.validTypes.size > 4 }) throw NotImplementedError("Instructions parameters with more than 4 possible types are not yet implemented")

    val argumentTypes = types.mapIndexed { index, type ->
        type.validTypes[(typeInformation.toInt() ushr (2 * index)) and 0b11]
    }.map { type ->
        InstructionArgumentEnum.entries.find { it.type == type }!!
    }

    return Pair(instructionEnum, argumentTypes.toTypedArray())
}
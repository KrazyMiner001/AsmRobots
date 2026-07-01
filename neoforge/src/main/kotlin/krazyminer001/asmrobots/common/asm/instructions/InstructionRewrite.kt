package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.InstructionEnum
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.*
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Condition
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Label
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Register

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
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
    ) : InstructionRewrite
    data class Movh(
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
    ) : InstructionRewrite
    data class Movb(
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
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
    data class Call(
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
    data object EI : InstructionRewrite
    data object DI : InstructionRewrite
    data object CI : InstructionRewrite

    fun toBytes(): ByteArray {
        return this.asEnum().toBytes(this)
    }

    companion object {
        fun Iterable<InstructionArgumentEnum>.byteLength(): Int = this.sumOf { it.ArgumentData.numBytes }
    }
}

fun InstructionRewrite.asEnum(): InstructionRewriteEnum = InstructionRewriteEnum.entries.find { it.name == this::class.simpleName }!!

fun InstructionRewrite.Companion.identifyInstruction(opcode: UByte, typeInformation: UByte): Pair<InstructionRewriteEnum, Array<InstructionArgumentEnum>> {
    val instructionEnum = InstructionRewriteEnum.entries.find { it.ordinal == opcode.toInt() }
    if (instructionEnum == null) throw IllegalArgumentException("Opcode $opcode does not correspond to an instruction")

    val types = instructionEnum.types

    var typeInformation = typeInformation.toInt()
    val argumentTypes = types.reversed().map { type ->
        val count = typeInformation % type.validTypes.size
        typeInformation -= count
        typeInformation /= type.validTypes.size

        InstructionArgumentEnum.entries.find { it.type == type.validTypes[count] }!!
    }.reversed()

    return Pair(instructionEnum, argumentTypes.toTypedArray())
}
package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.EnumerateInstructions
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.*
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Condition
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Label
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument.Register

@EnumerateInstructions(ArgumentType::class)
sealed interface Instruction {
    data class Add(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Sub(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Mul(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Mulh(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Div(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Rem(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Sll(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Srl(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Sra(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class Mov(
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
    ) : Instruction
    data class Movh(
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
    ) : Instruction
    data class Movb(
        val target: @ArgumentType(Register::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class, Pointer::class,
            Label::class
        ) InstructionArgument,
    ) : Instruction
    data object Ret : Instruction
    data object Nop : Instruction
    data class Push(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : Instruction
    data class Pop(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : Instruction
    data class Pushh(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : Instruction
    data class Poph(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : Instruction
    data class Pushb(
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : Instruction
    data class Popb(
        val target: @ArgumentType(Register::class) InstructionArgument
    ) : Instruction
    data object Halt : Instruction
    data class In(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val ioPortAddress: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : Instruction
    data class Out(
        val ioPortAddress: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val value: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument
    ) : Instruction
    data class Jump(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument
    ) : Instruction
    data class Call(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument
    ) : Instruction
    data class And(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : Instruction
    data class Or(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : Instruction
    data class Xor(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : Instruction
    data class Not(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : Instruction
    data class JCond(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument,
        val condition: @ArgumentType(Condition::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data object EI : Instruction
    data object DI : Instruction
    data object CI : Instruction

    //region Floating Point Extensions
    data class FAdd(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FSub(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FMul(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FDiv(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FSqrt(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FFMA(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FRem(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FMin(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FMax(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FNext(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FPrev(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FAbs(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FLog(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FExp(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FJCond(
        val address: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument,
        val condition: @ArgumentType(Condition::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class FToI(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    data class IToF(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : Instruction
    //endregion

    //region Memory Map Extensions
    data class MapIO(
        val outIdentifier: @ArgumentType(Register::class) InstructionArgument,
        val startAddress: @ArgumentType(Register::class, Immediate32::class, Label::class) InstructionArgument,
        val size: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
        val parameter: @ArgumentType(Register::class, Immediate32::class) InstructionArgument,
    ) : Instruction
    data class Unmap(
        val identifier: @ArgumentType(Register::class) InstructionArgument
    ) : Instruction
    //endregion

    fun toBytes(): ByteArray {
        return this.asEnum().toBytes(this)
    }

    companion object {
        fun Iterable<InstructionArgumentEnum>.byteLength(): Int = this.sumOf { it.ArgumentData.numBytes }
    }
}

fun Instruction.asEnum(): InstructionEnum = InstructionEnum.entries.find { it.name == this::class.simpleName }!!

fun Instruction.Companion.identifyInstruction(opcode: UByte, typeInformation: UByte): Pair<InstructionEnum, Array<InstructionArgumentEnum>> {
    val instructionEnum = InstructionEnum.entries.find { it.ordinal == opcode.toInt() }
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
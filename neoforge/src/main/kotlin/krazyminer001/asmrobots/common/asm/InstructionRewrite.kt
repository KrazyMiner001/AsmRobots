package krazyminer001.asmrobots.common.asm

import com.google.common.base.Splitter
import krazyminer001.asmrobots.annotations.InstructionEnum
import krazyminer001.asmrobots.common.asm.InstructionArgument.Pointer
import kotlin.collections.mapIndexed
import krazyminer001.asmrobots.common.asm.InstructionArgument.Register as Reg
import krazyminer001.asmrobots.common.asm.InstructionArgument.Immediate32 as Imm
import krazyminer001.asmrobots.common.asm.InstructionArgument.ImmediateFloat32 as ImmFloat

@InstructionEnum(ArgumentType::class)
sealed interface InstructionRewrite {
    data class Add(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sub(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mul(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mulh(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Div(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Rem(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sll(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Srl(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Sra(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val arg2: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
    ) : InstructionRewrite
    data class Mov(
        val target: @ArgumentType(Reg::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class, Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data class Movh(
        val target: @ArgumentType(Reg::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class, Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data class Movb(
        val target: @ArgumentType(Reg::class, Pointer::class) InstructionArgument,
        val arg1: @ArgumentType(Reg::class, Imm::class, ImmFloat::class, Pointer::class) InstructionArgument,
    ) : InstructionRewrite
    data object Ret : InstructionRewrite
    data class Syscall(
        val call: @ArgumentType(InstructionArgument.Syscall::class) InstructionArgument
    ) : InstructionRewrite
    data object Nop : InstructionRewrite
    data class Push(
        val value: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument
    ) : InstructionRewrite
    data class Pop(
        val target: @ArgumentType(Reg::class) InstructionArgument
    ) : InstructionRewrite
    data object Halt : InstructionRewrite
    data class In(
        val target: @ArgumentType(Reg::class) InstructionArgument,
        val ioPortAddress: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument
    ) : InstructionRewrite
    data class Out(
        val ioPortAddress: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument,
        val value: @ArgumentType(Reg::class, Imm::class, ImmFloat::class) InstructionArgument
    ) : InstructionRewrite

    companion object {
        fun Array<InstructionArgumentEnum>.byteLength(): Int = this.sumOf { it.ArgumentData.numBytes }
    }
}

fun InstructionRewrite.asEnum(): InstructionRewriteEnum = InstructionRewriteEnum.entries.find { it.name == this::class.simpleName }!!

fun InstructionRewrite.Companion.tryParse(string: String): AsmResult<InstructionRewrite, AsmError.ParseError> {
    val mnemonic = string.substringBefore(" ")
    val components = Splitter.on(", ")
        .omitEmptyStrings()
        .split(string.substringAfter(" ", ""))
        .toList()
        .map { Pair(InstructionArgument.parse(it), it) }
        .also { pairs ->
            val nulls = pairs.filter { it.first == null }
            if (nulls.isNotEmpty()) return AsmResult.Failure(AsmError.ParseError.InvalidInstructionArguments(*pairs.map { it.second }.toTypedArray()))
        }
        .map { it.first }
        .filterIsInstance<InstructionArgument>()
        .toTypedArray()

    val instructionType = InstructionRewriteEnum.entries.find { it.name.lowercase() == mnemonic }
    if (instructionType !is InstructionRewriteEnum) return AsmResult.Failure(AsmError.ParseError.InstructionNotFound(mnemonic))

    if (!instructionType.isValid(*components)) return AsmResult.Failure(AsmError.ParseError.InvalidInstructionArgumentsFor(mnemonic, components.joinToString()))

    return AsmResult.Success(instructionType.create(*components))
}

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
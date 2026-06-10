package krazyminer001.asmrobots.common.asm

import com.google.common.base.Splitter
import krazyminer001.asmrobots.annotations.InstructionEnum
import krazyminer001.asmrobots.common.asm.InstructionArgument.Pointer
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
        fun tryParse(string: String): Result<InstructionRewrite> {
            val mnemonic = string.substringBefore(" ")
            val components = Splitter.on(", ")
                .omitEmptyStrings()
                .split(string.substringAfter(" ", ""))
                .toList()
                .map { InstructionArgument.parse(it) }
                .also {
                    val nulls = it.filter { argument -> argument == null }
                    if (nulls.isNotEmpty()) return TODO("proper exception here")
                }
                .filterIsInstance<InstructionArgument>()
                .toTypedArray()

            val instructionType = InstructionRewriteEnum.entries.find { it.name.lowercase() == mnemonic }
            if (instructionType !is InstructionRewriteEnum) return Result.failure(InstructionNotFoundException(mnemonic))

            if (!instructionType.isValid(*components)) return Result.failure(Exception("Invalid instruction arguments"))

            return Result.success(instructionType.create(*components))
        }
    }
}
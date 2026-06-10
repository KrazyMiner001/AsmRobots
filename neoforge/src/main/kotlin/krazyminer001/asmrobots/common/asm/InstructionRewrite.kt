package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.InstructionEnum
import krazyminer001.asmrobots.common.asm.InstructionArgument.Register
import krazyminer001.asmrobots.common.asm.InstructionArgument.Immediate32
import krazyminer001.asmrobots.common.asm.InstructionArgument.ImmediateFloat32

@InstructionEnum(ArgumentType::class)
sealed interface InstructionRewrite {
    data class Add(
        val target: @ArgumentType(Register::class) InstructionArgument,
        val arg1: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
        val arg2: @ArgumentType(Register::class, Immediate32::class, ImmediateFloat32::class) InstructionArgument,
    ) : InstructionRewrite
}
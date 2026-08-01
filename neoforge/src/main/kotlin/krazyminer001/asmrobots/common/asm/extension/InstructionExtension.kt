package krazyminer001.asmrobots.common.asm.extension

import krazyminer001.asmrobots.common.asm.instructions.Instruction
import krazyminer001.asmrobots.common.asm.instructions.InstructionEnum
import krazyminer001.asmrobots.common.asm.instructions.InstructionEnum.*
import krazyminer001.asmrobots.common.asm.instructions.asEnum

enum class InstructionExtension(vararg val instructions: InstructionEnum) : Extension {
    FLOATING_POINT_ARITHMETIC(
        FAdd, FSub, FMul, FDiv, FSqrt, FFMA, FRem, FMin, FMax, FNext, FPrev, FAbs, FLog, FExp, FJCond, FToI, IToF
    ),
    MEMORY_MAPPING(
        MapIO, Unmap
    ),
    ;

    operator fun contains(instruction: Instruction): Boolean {
        return instruction.asEnum() in instructions
    }
}
package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.ParsableEnumerated
import krazyminer001.asmrobots.common.asm.Instruction
import krazyminer001.asmrobots.common.asm.Immediate as Imm
import krazyminer001.asmrobots.common.asm.Instruction as I
import krazyminer001.asmrobots.common.asm.Register as Reg

@ParsableEnumerated
sealed interface Instruction {
    data class Add(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Addi(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Neg(val target: Reg, val arg1: Reg) : I
    data class Sub(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Mul(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Mulh(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Mulhu(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Muli(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Div(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Divi(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Rem(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Remi(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Sll(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Slli(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Srl(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Srli(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Sra(val target: Reg, val arg1: Reg, val arg2: Reg) : I
    data class Srai(val target: Reg, val arg1: Reg, val arg2: Imm) : I
    data class Li(val target: Reg, val arg1: Imm) : I
    

    companion object {
        fun tryParse(code: String): Result<Instruction> {
            val mnemonic = code.substringBefore(" ")
            val components = code.substringAfter(" ")
            val instruction = InstructionEnum.entries.find { it.name.lowercase() == mnemonic }

            if (instruction !is InstructionEnum) return Result.failure(InstructionNotFoundException(mnemonic))

            return try {
                Result.success(instruction(components))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}


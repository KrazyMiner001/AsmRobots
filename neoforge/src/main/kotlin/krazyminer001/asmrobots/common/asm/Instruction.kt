package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.ParsableEnumerated

@ParsableEnumerated
sealed interface Instruction {
    data class AddR(val target: Register, val arg1: Register) : Instruction
    data class AddRR(val target: Register, val arg1: Register, val arg2: Register) : Instruction
    data class AddI(val target: Register, val arg1: Literal) : Instruction
    data class AddRI(val target: Register, val arg1: Register, val arg2: Literal) : Instruction

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

interface Parsable<T> {
    fun parse(string: String): T
}
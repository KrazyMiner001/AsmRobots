package krazyminer001.asmrobots.common.asm

import com.google.common.base.Splitter
import krazyminer001.asmrobots.annotations.ParsableEnumerated
import krazyminer001.asmrobots.common.asm.InstructionOLD
import krazyminer001.asmrobots.common.asm.Immediate as Imm
import krazyminer001.asmrobots.common.asm.InstructionOLD as I
import krazyminer001.asmrobots.common.asm.Register as Reg

@ParsableEnumerated
sealed interface InstructionOLD {
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
    data class Lw(val target: Reg, val arg1: Pointer) : I
    data class Lh(val target: Reg, val arg1: Pointer) : I
    data class Lb(val target: Reg, val arg1: Pointer) : I
    data class La(val target: Reg, val arg1: Label) : I
    data class Sw(val target: Pointer, val arg1: Reg) : I
    data class Sh(val target: Pointer, val arg1: Reg) : I
    data class Sb(val target: Pointer, val arg1: Reg) : I
    data class Call(val address: Label) : I
    data object Ret : I
    data class Syscall(val call: krazyminer001.asmrobots.common.asm.Syscall) : I
    data class Mov(val target: Reg, val arg1: Reg) : I
    data object Nop : I
    data class Push(val arg1: Reg) : I
    data class Pushi(val arg1: Imm) : I
    data class Pop(val target: Reg) : I
    data object Halt : I
    data class In(val target: Reg, val ioAddress: Reg) : I
    data class Ini(val target: Reg, val ioAddress: Imm) : I
    data class Out(val targetIOAddress: Reg, val arg1: Reg) : I
    data class Outi(val targetIOAddress: Imm, val arg1: Reg) : I
    data class Outii(val targetIOAddress: Imm, val arg1: Imm) : I

    companion object {
        fun tryParse(code: String): Result<InstructionOLD> {
            val mnemonic = code.substringBefore(" ")
            val components = Splitter.on(", ")
                .omitEmptyStrings()
                .split(code.substringAfter(" ", ""))
                .toList()
                .toTypedArray()
            val instruction = InstructionOLDEnum.entries.find { it.name.lowercase() == mnemonic }

            if (instruction !is InstructionOLDEnum) return Result.failure(InstructionNotFoundException(mnemonic))

            return try {
                Result.success(instruction(components))
            } catch (_: IllegalArgumentException) {
                Result.failure(InvalidInstructionParameterCount(instruction.name, components.size, instruction::invoke.parameters.size))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}


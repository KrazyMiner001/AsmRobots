package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewrite
import krazyminer001.asmrobots.common.asm.instructions.Register.*
import krazyminer001.asmrobots.common.asm.instructions.fromBytes
import krazyminer001.asmrobots.common.asm.instructions.identityInstruction
import kotlin.experimental.or

class Program(private val callback: ProgramCallback, memorySize: Int = 8192) {
    private val memory: ByteArray = ByteArray(memorySize) { 0 }
    private val callStack: MutableList<Int> = mutableListOf()
    private val stack: MutableList<Int> = mutableListOf()
    private val reg: RegisterStorage = RegisterStorage()
    private val labels: MutableMap<String, Int> = mutableMapOf()

    fun initMemoryAndLabels(initialMemory: ByteArray = byteArrayOf(), labels: Map<String, Int> = mapOf()) {
        initialMemory.copyInto(memory)
        this.labels.putAll(labels)
    }

    fun step() {
        val (instruction, argumentTypes) = InstructionRewrite.identityInstruction(
            memory[reg[PC]].toUByte(),
            memory[reg[PC] + 1].toUByte()
        )
        var counter = 0
        val arguments = argumentTypes.map {
            val argument = InstructionArgument.fromBytes(
                memory.copyOfRange(reg[PC] + 2 + counter, reg[PC] + 2 + counter + it.ArgumentData.numBytes),
                it
            )
            counter += it.ArgumentData.numBytes
            return@map argument
        }.toTypedArray()
        reg[PC] += 2 + counter

        val it = instruction.create(*arguments)
        with(it) {
            when (this) {
                is InstructionRewrite.Add -> target.wordValue = arg1.wordValue + arg2.wordValue
                is InstructionRewrite.Div -> target.wordValue = arg1.wordValue / arg2.wordValue
                InstructionRewrite.Halt -> callback.halt()
                is InstructionRewrite.In -> target.wordValue = callback[ioPortAddress.wordValue]
                is InstructionRewrite.Mov -> target.wordValue = arg1.wordValue
                is InstructionRewrite.Movb -> target.byteValue = arg1.byteValue
                is InstructionRewrite.Movh -> target.halfValue = arg1.halfValue
                is InstructionRewrite.Mul -> target.wordValue = arg1.wordValue * arg2.wordValue
                is InstructionRewrite.Mulh -> target.wordValue = (arg1.wordValue.toLong() * arg2.wordValue.toLong()).ushr(32).toInt()
                InstructionRewrite.Nop -> {}
                is InstructionRewrite.Out -> callback[ioPortAddress.wordValue] = value.wordValue
                is InstructionRewrite.Pop -> target.wordValue = stack.removeLast()
                is InstructionRewrite.Push -> stack.add(value.wordValue)
                is InstructionRewrite.Rem -> target.wordValue = arg1.wordValue % arg2.wordValue
                InstructionRewrite.Ret -> reg[PC] = callStack.removeLast()
                is InstructionRewrite.Sll -> target.wordValue = arg1.wordValue shl arg2.wordValue
                is InstructionRewrite.Sra -> target.wordValue = arg1.wordValue shr arg2.wordValue
                is InstructionRewrite.Srl -> target.wordValue = arg1.wordValue ushr arg2.wordValue
                is InstructionRewrite.Sub -> target.wordValue = arg1.wordValue - arg2.wordValue
                is InstructionRewrite.Syscall -> TODO()
            }
        }

    }

    private fun ByteArray.getWord(address: Int): Int {
        return this[address].toUByte().toInt() or
                (this[address + 1].toUByte().toInt() shl 8) or
                (this[address + 2].toUByte().toInt() shl 16) or
                (this[address + 3].toUByte().toInt() shl 24)
    }

    private fun ByteArray.getHalf(address: Int): Short {
        return this[address].toUByte().toShort() or
                (this[address + 1].toUByte().toInt() shl 8).toShort()
    }

    private fun ByteArray.setWord(address: Int, value: Int) {
        this[address] = value.toByte()
        this[address + 1] = (value ushr 8).toByte()
        this[address + 2] = (value ushr 16).toByte()
        this[address + 3] = (value ushr 24).toByte()
    }

    private fun ByteArray.setHalf(address: Int, value: Short) {
        this[address] = value.toByte()
        this[address + 1] = (value.toUInt() shr 8).toByte()
    }

    var InstructionArgument.wordValue: Int
        get() = when (this) {
            is InstructionArgument.Immediate32 -> this.value
            is InstructionArgument.ImmediateFloat32 -> this.value.toBits()
            is InstructionArgument.Pointer -> memory.getWord(reg[this.register] + this.offset.value)
            is InstructionArgument.Register -> reg[this.register]
            is InstructionArgument.Syscall -> this.syscall.ordinal
        }
        set(value) = when (this) {
            is InstructionArgument.Immediate32 -> throw IllegalArgumentException()
            is InstructionArgument.ImmediateFloat32 -> throw IllegalArgumentException()
            is InstructionArgument.Pointer -> memory.setWord(reg[this.register] + this.offset.value, value)
            is InstructionArgument.Register -> reg[this.register] = value
            is InstructionArgument.Syscall -> throw IllegalArgumentException()
        }

    var InstructionArgument.halfValue: Short
        get() = when (this) {
            is InstructionArgument.Pointer -> memory.getHalf(reg[this.register] + this.offset.value)
            else -> this.wordValue.toShort()
        }
        set(value) = when (this) {
            is InstructionArgument.Pointer -> memory.setHalf(reg[this.register] + this.offset.value, value)
            else -> this.wordValue = value.toUShort().toInt()
        }

    var InstructionArgument.byteValue: Byte
        get() = when (this) {
            is InstructionArgument.Pointer -> memory[reg[this.register] + this.offset.value]
            else -> this.wordValue.toByte()
        }
        set(value) = when (this) {
            is InstructionArgument.Pointer -> memory[reg[this.register] + this.offset.value] = value
            else -> this.wordValue = value.toUByte().toInt()
        }

}
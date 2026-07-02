package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.*
import krazyminer001.asmrobots.common.asm.instructions.Register.PC
import krazyminer001.asmrobots.common.asm.instructions.Register.SP
import kotlin.experimental.or

class Program(private val callback: ProgramCallback, memorySize: Int = 8192) {
    private val memory: ByteArray = ByteArray(memorySize) { 0 }
    private val callStack: MutableList<Int> = mutableListOf()
    private val reg: RegisterStorage = RegisterStorage()
    private val labels: MutableMap<String, Int> = mutableMapOf()
    private var interruptsEnabled = false
    private val pendingInterrupts: MutableList<Int> = mutableListOf()

    init {
        reg[SP] = memorySize - 1
    }

    fun initMemoryAndLabels(initialMemory: ByteArray = byteArrayOf(), labels: Map<String, Int> = mapOf()) {
        initialMemory.copyInto(memory)
        this.labels.putAll(labels)
        reg[PC] = this.labels.getOrElse("_start") { 0 }
    }

    fun interrupt(id: Int) {
        pendingInterrupts.add(id)
    }

    fun step() {
        if (interruptsEnabled && pendingInterrupts.isNotEmpty()) {
            val interrupt = pendingInterrupts.removeLast()
            callStack.add(reg[PC])
            push(interrupt)
            reg[PC] = labels.getOrElse("_int") { 0 }
            interruptsEnabled = false
        }

        val (instruction, argumentTypes) = Instruction.identifyInstruction(
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
                is Instruction.Add -> target.wordValue = arg1.wordValue + arg2.wordValue
                is Instruction.Div -> target.wordValue = arg1.wordValue / arg2.wordValue
                Instruction.Halt -> callback.halt()
                is Instruction.In -> target.wordValue = callback[ioPortAddress.wordValue]
                is Instruction.Mov -> target.wordValue = arg1.wordValue
                is Instruction.Movb -> target.byteValue = arg1.byteValue
                is Instruction.Movh -> target.halfValue = arg1.halfValue
                is Instruction.Mul -> target.wordValue = arg1.wordValue * arg2.wordValue
                is Instruction.Mulh -> target.wordValue =
                    (arg1.wordValue.toLong() * arg2.wordValue.toLong()).ushr(32).toInt()

                Instruction.Nop -> {}
                is Instruction.Out -> callback[ioPortAddress.wordValue] = value.wordValue
                is Instruction.Pop -> target.wordValue = pop()
                is Instruction.Push -> push(value.wordValue)
                is Instruction.Poph -> target.wordValue = popHalf().toUShort().toInt()
                is Instruction.Pushh -> pushHalf(value.wordValue.toShort())
                is Instruction.Popb -> target.wordValue = popByte().toUByte().toInt()
                is Instruction.Pushb -> pushByte(value.wordValue.toByte())
                is Instruction.Rem -> target.wordValue = arg1.wordValue % arg2.wordValue
                Instruction.Ret -> reg[PC] = callStack.removeLast()
                is Instruction.Sll -> target.wordValue = arg1.wordValue shl arg2.wordValue
                is Instruction.Sra -> target.wordValue = arg1.wordValue shr arg2.wordValue
                is Instruction.Srl -> target.wordValue = arg1.wordValue ushr arg2.wordValue
                is Instruction.Sub -> target.wordValue = arg1.wordValue - arg2.wordValue
                is Instruction.Jump -> reg[PC] = address.wordValue
                is Instruction.Call -> {
                    callStack.add(reg[PC])
                    reg[PC] = address.wordValue
                }
                is Instruction.And -> target.wordValue = arg1.wordValue and arg2.wordValue
                is Instruction.JCond -> {
                    if (when ((condition as InstructionArgument.Condition).condition) {
                            Condition.EQ -> arg1.wordValue == arg2.wordValue
                            Condition.LT -> arg1.wordValue < arg2.wordValue
                            Condition.LE -> arg1.wordValue <= arg2.wordValue
                            Condition.GT -> arg1.wordValue > arg2.wordValue
                            Condition.GE -> arg1.wordValue >= arg2.wordValue
                        }
                    ) {
                        reg[PC] = address.wordValue
                    }
                }
                is Instruction.Not -> target.wordValue = arg1.wordValue.inv()
                is Instruction.Or -> target.wordValue = arg1.wordValue or arg2.wordValue
                is Instruction.Xor -> target.wordValue = arg1.wordValue xor arg2.wordValue
                Instruction.DI -> interruptsEnabled = false
                Instruction.EI -> interruptsEnabled  = true
                Instruction.CI -> pendingInterrupts.clear()
            }
        }
    }

    private fun pop(): Int {
        val num = memory.getWord(reg[SP])
        reg[SP] += 4
        return num
    }

    private fun popHalf(): Short {
        val num = memory.getHalf(reg[SP])
        reg[SP] += 2
        return num
    }

    private fun popByte(): Byte {
        val num = memory[reg[SP]]
        reg[SP] += 1
        return num
    }

    private fun push(num: Int) {
        reg[SP] -= 4
        memory.setWord(reg[SP], num)
    }

    private fun pushHalf(num: Short) {
        reg[SP] -= 2
        memory.setHalf(reg[SP], num)
    }

    private fun pushByte(num: Byte) {
        reg[SP] -= 1
        memory[reg[SP]] = num
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
            is InstructionArgument.Label -> this.value
            else -> throw IllegalArgumentException()
        }
        set(value) = when (this) {
            is InstructionArgument.Immediate32 -> throw IllegalArgumentException()
            is InstructionArgument.ImmediateFloat32 -> throw IllegalArgumentException()
            is InstructionArgument.Pointer -> memory.setWord(reg[this.register] + this.offset.value, value)
            is InstructionArgument.Register -> reg[this.register] = value
            is InstructionArgument.Label -> throw IllegalArgumentException()
            else -> throw IllegalArgumentException()
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
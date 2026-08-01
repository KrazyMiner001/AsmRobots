package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.extension.InstructionExtension.MEMORY_MAPPING
import krazyminer001.asmrobots.common.asm.instructions.*
import krazyminer001.asmrobots.common.asm.instructions.Instruction.*
import krazyminer001.asmrobots.common.asm.instructions.Register.PC
import krazyminer001.asmrobots.common.asm.instructions.Register.SP
import kotlin.experimental.or
import kotlin.math.*
import krazyminer001.asmrobots.common.asm.extension.InstructionExtension.FLOATING_POINT_ARITHMETIC as FP

class Program(private val callback: ProgramCallback, memorySize: Int = 8192) {
    private val memory: Memory = Memory(memorySize) { MEMORY_MAPPING in callback }
    private val callStack: MutableList<Int> = mutableListOf()
    private val reg: RegisterStorage = RegisterStorage()
    private val labels: MutableMap<String, Int> = mutableMapOf()
    private var interruptsEnabled = false
    private val pendingInterrupts: MutableList<Int> = mutableListOf()

    init {
        reg[SP] = memorySize - 1
    }

    fun initMemoryAndLabels(initialMemory: ByteArray = byteArrayOf(), labels: Map<String, Int> = mapOf()) {
        memory.initRealMemory(initialMemory)
        this.labels.putAll(labels)
        reg[PC] = this.labels.getOrElse("_start") { 0 }
    }

    fun interrupt(id: Int) {
        pendingInterrupts.add(id)
    }

    fun step(): AsmError.RuntimeError? {
        if (interruptsEnabled && pendingInterrupts.isNotEmpty()) {
            val interrupt = pendingInterrupts.removeLast()
            callStack.add(reg[PC])
            push(interrupt)
            reg[PC] = labels.getOrElse("_int") { 0 }
            interruptsEnabled = false
        }

        val oldPC = reg[PC]

        val instruction = runCatching {
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

            instruction.create(*arguments)
        }.getOrElse {
            return AsmError.RuntimeError.CouldNotParseInstruction(oldPC)
        }

        runCatching {
            with(instruction) {
                when (this) {
                    is Add -> target.wordValue = arg1.wordValue + arg2.wordValue
                    is Div -> target.wordValue = arg1.wordValue / arg2.wordValue
                    Halt -> callback.halt()
                    is In -> target.wordValue = callback[ioPortAddress.wordValue]
                    is Mov -> target.wordValue = arg1.wordValue
                    is Movb -> target.byteValue = arg1.byteValue
                    is Movh -> target.halfValue = arg1.halfValue
                    is Mul -> target.wordValue = arg1.wordValue * arg2.wordValue
                    is Mulh -> target.wordValue =
                        (arg1.wordValue.toLong() * arg2.wordValue.toLong()).ushr(32).toInt()

                    Nop -> {}
                    is Out -> callback[ioPortAddress.wordValue] = value.wordValue
                    is Pop -> target.wordValue = pop()
                    is Push -> push(value.wordValue)
                    is Poph -> target.wordValue = popHalf().toUShort().toInt()
                    is Pushh -> pushHalf(value.wordValue.toShort())
                    is Popb -> target.wordValue = popByte().toUByte().toInt()
                    is Pushb -> pushByte(value.wordValue.toByte())
                    is Rem -> target.wordValue = arg1.wordValue % arg2.wordValue
                    Ret -> reg[PC] = callStack.removeLast()
                    is Sll -> target.wordValue = arg1.wordValue shl arg2.wordValue
                    is Sra -> target.wordValue = arg1.wordValue shr arg2.wordValue
                    is Srl -> target.wordValue = arg1.wordValue ushr arg2.wordValue
                    is Sub -> target.wordValue = arg1.wordValue - arg2.wordValue
                    is Jump -> reg[PC] = address.wordValue
                    is Call -> {
                        callStack.add(reg[PC])
                        reg[PC] = address.wordValue
                    }

                    is And -> target.wordValue = arg1.wordValue and arg2.wordValue
                    is JCond -> {
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

                    is Not -> target.wordValue = arg1.wordValue.inv()
                    is Or -> target.wordValue = arg1.wordValue or arg2.wordValue
                    is Xor -> target.wordValue = arg1.wordValue xor arg2.wordValue
                    DI -> interruptsEnabled = false
                    EI -> interruptsEnabled = true
                    CI -> pendingInterrupts.clear()
                    in FP if FP !in callback ->
                        return AsmError
                            .RuntimeError
                            .ExtensionNotPresent(instruction, FP)
                    is FAdd if FP in callback -> target.floatValue = arg1.floatValue + arg2.floatValue
                    is FDiv if FP in callback -> target.floatValue = arg1.floatValue / arg2.floatValue
                    is FFMA if FP in callback -> target.floatValue += arg1.floatValue * arg2.floatValue
                    is FMax if FP in callback -> target.floatValue = max(arg1.floatValue, arg2.floatValue)
                    is FMin if FP in callback -> target.floatValue = min(arg1.floatValue, arg2.floatValue)
                    is FMul if FP in callback -> target.floatValue = arg1.floatValue * arg2.floatValue
                    is FNext if FP in callback -> target.floatValue = arg.floatValue.nextUp()
                    is FRem if FP in callback -> target.floatValue = arg1.floatValue.IEEErem(arg2.floatValue)
                    is FSqrt if FP in callback -> target.floatValue = sqrt(arg.floatValue)
                    is FSub if FP in callback -> target.floatValue = arg1.floatValue - arg2.floatValue
                    is FAbs if FP in callback -> target.floatValue = arg.floatValue.absoluteValue
                    is FLog if FP in callback -> target.floatValue = log2(arg.floatValue)
                    is FExp if FP in callback -> target.floatValue = arg.floatValue.pow(2)
                    is FJCond if FP in callback -> {
                        if (when ((condition as InstructionArgument.Condition).condition) {
                                Condition.EQ -> arg1.floatValue == arg2.floatValue
                                Condition.LT -> arg1.floatValue < arg2.floatValue
                                Condition.LE -> arg1.floatValue <= arg2.floatValue
                                Condition.GT -> arg1.floatValue > arg2.floatValue
                                Condition.GE -> arg1.floatValue >= arg2.floatValue
                            }
                        ) {
                            reg[PC] = address.wordValue
                        }
                    }
                    is FToI if FP in callback -> target.wordValue = arg.floatValue.toInt()
                    is IToF if FP in callback -> target.floatValue = arg.wordValue.toFloat()
                    in MEMORY_MAPPING if MEMORY_MAPPING !in callback -> {
                        return AsmError
                            .RuntimeError
                            .ExtensionNotPresent(instruction, MEMORY_MAPPING)
                    }
                    is MapIO if MEMORY_MAPPING in callback -> {
                        val memoryMap = memory.createMemoryMap(
                            startAddress.wordValue,
                            size.wordValue,
                            { address -> callback.getMappedMemory(parameter.wordValue, address) },
                            { address, value -> callback.setMappedMemory(parameter.wordValue, address, value) }
                        )
                        outIdentifier.wordValue = memoryMap.id
                    }
                    is Unmap if MEMORY_MAPPING in callback -> {
                        memory.removeMemoryMap(Memory.MemoryMapId(identifier.wordValue))
                    }
                    else -> {}
                }
            }
        }.onFailure {
            return AsmError.RuntimeError.ErrorExecutingInstruction(oldPC, instruction)
        }

        return null
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

    private fun Memory.getWord(address: Int): Int {
        return this[address].toUByte().toInt() or
                (this[address + 1].toUByte().toInt() shl 8) or
                (this[address + 2].toUByte().toInt() shl 16) or
                (this[address + 3].toUByte().toInt() shl 24)
    }

    private fun Memory.getHalf(address: Int): Short {
        return this[address].toUByte().toShort() or
                (this[address + 1].toUByte().toInt() shl 8).toShort()
    }

    private fun Memory.setWord(address: Int, value: Int) {
        this[address] = value.toByte()
        this[address + 1] = (value ushr 8).toByte()
        this[address + 2] = (value ushr 16).toByte()
        this[address + 3] = (value ushr 24).toByte()
    }

    private fun Memory.setHalf(address: Int, value: Short) {
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

    var InstructionArgument.floatValue: Float
        get() = Float.fromBits(this.wordValue)
        set(value) { this.wordValue = value.toRawBits() }

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
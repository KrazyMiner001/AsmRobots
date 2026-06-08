package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.Register.*
import krazyminer001.asmrobots.common.asm.Instruction.*
import kotlin.experimental.or

class Program(private val code: Code, memorySize: Int = 8192) {
    private val memory: ByteArray = ByteArray(memorySize) { 0 }
    private val callStack: MutableList<Int> = mutableListOf()
    private val stack: MutableList<Int> = mutableListOf()
    private val reg: RegisterStorage = RegisterStorage()

    init {
        runCatching {
            reg[PC] = code[Label("_start")]
        }
    }

    fun step() {
        with(reg) {
            with(code[PC.value++]) {
                when (this) {
                    is Add -> target.value = arg1.value + arg2.value
                    is Addi -> target.value = arg1.value + arg2.value
                    is Div -> target.value = arg1.value / arg2.value
                    is Divi -> target.value = arg1.value / arg2.value
                    is Mul -> target.value = arg1.value * arg2.value
                    is Mulh -> target.value = ((arg1.value.toLong() * arg2.value.toLong()) shr 32).toInt()
                    is Mulhu -> target.value = ((arg1.value.toULong() * arg2.value.toULong()) shr 32).toInt()
                    is Muli -> target.value = arg1.value * arg2.value
                    is Neg -> target.value = -arg1.value
                    is Rem -> target.value = arg1.value % arg2.value
                    is Remi -> target.value = arg1.value % arg2.value
                    is Sub -> target.value = arg1.value - arg2.value
                    is Li -> target.value = arg1.value
                    is Sll -> target.value = arg1.value shl arg2.value
                    is Slli -> target.value = arg1.value shl arg2.value
                    is Sra -> target.value = arg1.value shr arg2.value
                    is Srai -> target.value = arg1.value shr arg2.value
                    is Srl -> target.value = arg1.value ushr arg2.value
                    is Srli -> target.value = arg1.value ushr arg2.value
                    is Call -> {
                        callStack.add(PC.value)
                        PC.value = code[address]
                    }
                    is La -> target.value = code[arg1]
                    is Lb -> target.value = memory[arg1.value].toUByte().toInt()
                    is Lh -> target.value = memory.getHalf(arg1.value).toUShort().toInt()
                    is Lw -> target.value = memory.getWord(arg1.value)
                    is Ret -> PC.value = callStack.removeLast()
                    is Sb -> memory[target.value] = arg1.value.toUByte().toByte()
                    is Sh -> memory.setHalf(target.value, arg1.value.toUShort().toShort())
                    is Sw -> memory.setWord(target.value, arg1.value)
                    is Mov -> target.value = arg1.value
                    is Nop -> {}
                    is Instruction.Syscall -> TODO()
                    is Pop -> target.value = stack.removeLast()
                    is Push -> stack.add(arg1.value)
                    is Pushi -> stack.add(arg1.value)
                }
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

    class Code {
        private val labels: MutableMap<Label, Int> = mutableMapOf()
        private val instructions: MutableList<Instruction> = mutableListOf()

        internal fun addLabel(label: Label) {
            labels[label] = instructions.size
        }

        internal fun addInstruction(instruction: Instruction) {
            instructions.add(instruction)
        }

        operator fun get(lineNumber: Int): Instruction {
            return instructions[lineNumber]
        }

        operator fun get(label: Label): Int {
            return labels[label]!!
        }

        override fun toString(): String {
            val stringBuilder = StringBuilder()
            instructions.forEachIndexed { index, instruction ->
                labels.filterValues { it == index }.keys.map { it.name + ":\n" }.forEach {
                    stringBuilder.append(it)
                }
                stringBuilder.append(instruction.toString() + "\n")
            }
            return stringBuilder.toString()
        }
    }
}

fun lex (code: String): Result<Program.Code> {
    val lines = code.split('\n')
    val code = Program.Code()
    val parseErrors = mutableListOf<ParseError>()
    lines.forEachIndexed { index, line ->
        val trimmed = line.trim()
        val matches = CommentRegex.matchEntire(trimmed)!!.groups
        //val comment = matches["comment"]?.value?.let(::Comment)
        val content = matches["content"]!!.value.trim()
        if (content.endsWith(":")) {
            code.addLabel(Label(content.dropLast(1)))
            return@forEachIndexed
        }
        if (content.isBlank()) {
            return@forEachIndexed
        }
        Instruction.tryParse(content).fold({
            code.addInstruction(it)
            return@forEachIndexed
        }) {
            parseErrors.add(ParseError(it, index))
        }
    }
    if (parseErrors.isNotEmpty()) return Result.failure(ParseErrors(*parseErrors.toTypedArray()))
    return Result.success(code)
}

val CommentRegex: Regex = "^(?<content>.*?)(?://(?<comment>.*))?$".toRegex()
package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewrite.Companion.byteLength
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewriteEnum
import krazyminer001.asmrobots.common.asm.instructions.asEnum
import krazyminer001.asmrobots.common.asm.instructions.Condition as ConditionEnum
import krazyminer001.asmrobots.common.asm.instructions.Register as RegisterEnum

object Assembler {
    fun lex(code: String): List<List<Lexeme>> {
        val lines = code.split('\n')

        fun parseArgument(
            string: String,
        ): Lexeme {
            val int = string.toIntOrNull()
            val float = string.toFloatOrNull()
            val condition = ConditionEnum.entries.find { it.name.lowercase() == string }
            val register = RegisterEnum.entries.find { it.name.lowercase() == string }
            return when {
                null != int -> Lexeme.Integer(int)
                null != float -> Lexeme.FloatNum(float, string)
                null != condition -> Lexeme.Condition(condition)
                null != register -> Lexeme.Register(register)
                else -> Lexeme.Identifier(string)
            }
        }

        return lines.map { line ->
            val lexemes = mutableListOf<Lexeme>()
            var partialString = ""
            line.forEach { char ->
                partialString += char

                when {
                    partialString.startsWith("//") -> {
                        val lastLexemeIndex = lexemes.lastIndex
                        if (lexemes.getOrNull(lastLexemeIndex) !is Lexeme.Comment) {
                            lexemes += Lexeme.Comment(partialString.drop(2))
                        } else {
                            lexemes[lastLexemeIndex] = Lexeme.Comment(partialString.drop(2))
                        }
                    }

                    partialString.startsWith("/") && partialString.length != 1 -> {
                        lexemes += Lexeme.Error(partialString)
                        partialString = ""
                    }

                    partialString.endsWith(":") -> {
                        val mainPart = partialString.dropLast(1)
                        if (mainPart.isNotEmpty()) {
                            lexemes += Lexeme.Identifier(mainPart)
                        }
                        lexemes += Lexeme.Colon
                        partialString = ""
                    }

                    partialString.endsWith(",") -> {
                        val mainPart = partialString.dropLast(1)
                        if (mainPart.isNotEmpty()) {
                            lexemes += parseArgument(mainPart)
                        }
                        lexemes += Lexeme.Comma
                        partialString = ""
                    }

                    partialString.endsWith(" ") -> {
                        val mainPart = partialString.dropLast(1)
                        if (mainPart.isNotEmpty()) {
                            val mnemonic = InstructionRewriteEnum.entries.find { it.name.lowercase() == mainPart }
                            lexemes += mnemonic?.let { Lexeme.Mnemonic(it) } ?: parseArgument(mainPart)
                        }
                        lexemes += Lexeme.Whitespace
                        partialString = ""
                    }

                    partialString.endsWith("(") -> {
                        val mainPart = partialString.dropLast(1)
                        val int = mainPart.toIntOrNull()
                        lexemes += if (int == null) Lexeme.Error(mainPart) else Lexeme.Integer(int)
                        lexemes += Lexeme.LeftBracket
                        partialString = ""
                    }

                    partialString.endsWith(")") -> {
                        val mainPart = partialString.dropLast(1)
                        val register = RegisterEnum.entries.find { it.name.lowercase() == mainPart }
                        lexemes += if (register == null) Lexeme.Error(mainPart) else Lexeme.Register(register)
                        lexemes += Lexeme.RightBracket
                        partialString = ""
                    }
                }
            }
            if (partialString.isNotEmpty() && lexemes.lastOrNull() !is Lexeme.Comment) {
                val mnemonic = InstructionRewriteEnum.entries.find { it.name.lowercase() == partialString }
                lexemes += mnemonic?.let { Lexeme.Mnemonic(it) } ?: parseArgument(partialString)
            }
            lexemes.toList()
        }
    }

    fun parse(line: List<Lexeme>): AsmResult<LexedLine, AsmError.ParseError> {
        if (line.any { it is Lexeme.Error }) TODO()
        val parts = line.filter { it !is Lexeme.Whitespace }

        if (parts.isEmpty()) return LexedLine(null, null).asSuccess()

        val comment = parts.lastOrNull().let { it as? Lexeme.Comment }?.text
        val firstPart = parts.firstOrNull()

        if (firstPart is Lexeme.Identifier && parts.getOrNull(1) is Lexeme.Colon) {
            return LexedLine(LexedLine.Content.Label(firstPart.text), comment).asSuccess()
        }

        if (firstPart is Lexeme.Mnemonic) {
            val arguments = mutableListOf<InstructionArgument>()
            val invalidArguments = mutableListOf<String>()
            var argumentLexemes = parts.drop(1).let { if (comment != null) it.dropLast(1) else it }
            while (argumentLexemes.isNotEmpty()) {
                val argument = argumentLexemes.takeWhile { it !is Lexeme.Comma }
                argumentLexemes = argumentLexemes.drop(argument.size + 1)

                if (argument.size == 1) {
                    val (lexeme) = argument
                    when (lexeme) {
                        is Lexeme.Register -> arguments += InstructionArgument.Register(lexeme.register)
                        is Lexeme.Identifier -> arguments += InstructionArgument.Label(-1, lexeme.text)
                        is Lexeme.Condition -> arguments += InstructionArgument.Condition(lexeme.condition)
                        is Lexeme.Integer -> arguments += InstructionArgument.Immediate32(lexeme.value)
                        is Lexeme.FloatNum -> arguments += InstructionArgument.ImmediateFloat32(lexeme.value)
                        else -> invalidArguments += lexeme.toString()
                    }
                } else if (argument.size == 4) {
                    val (offset, openBracket, register, closeBracket) = argument
                    if (offset is Lexeme.Integer && openBracket is Lexeme.LeftBracket && register is Lexeme.Register && closeBracket is Lexeme.RightBracket) {
                        arguments += InstructionArgument.Pointer(
                            register.register,
                            InstructionArgument.Immediate32(offset.value)
                        )
                    } else {
                        invalidArguments += argument.joinToString("")
                    }
                } else {
                    invalidArguments += argument.joinToString("")
                }
            }
            if (invalidArguments.isNotEmpty())
                return AsmResult.Failure(AsmError.ParseError.InvalidInstructionArguments(invalidArguments))
            return LexedLine(LexedLine.Content.Instruction(firstPart.mnemonic, arguments), comment).asSuccess()
        }

        if (parts.size == 1) return LexedLine(null, comment).asSuccess()

        return AsmResult.Failure(AsmError.ParseError.UnparsableLine(line.joinToString("")))
    }

    fun assemble(lines: List<LexedLine>): AsmResult<Pair<ByteArray, Map<String, Int>>, AsmError.ParseError.ParseErrors> {
        val labels = mutableMapOf<String, Int>()
        val pendingLabels = mutableListOf<LexedLine.Content.Label>()
        val instructions = mutableListOf<Pair<LexedLine.Content.Instruction, Int>>()
        lines.forEachIndexed { index, line ->
            when (line.content) {
                is LexedLine.Content.Label -> pendingLabels.add(line.content)
                is LexedLine.Content.Instruction -> {
                    labels.putAll(pendingLabels.map { Pair(it.name, instructions.size) })
                    pendingLabels.clear()
                    instructions.add(Pair(line.content, index))
                }

                else -> {}
            }
        }

        val parseErrors = mutableListOf<Pair<AsmError.ParseError, Int>>()

        val instructionIndexToRam = mutableMapOf<Int, Int>()

        var memorySize = 0
        instructions.forEachIndexed { index, (instruction, _) ->
            instructionIndexToRam[index] = memorySize
            memorySize += instruction.arguments.map { it.asEnum() }.byteLength() + 2
        }

        val memory = mutableListOf<Byte>()
        val labelsToRam = labels.mapValues { instructionIndexToRam[it.value]!! }

        instructions.map { (instruction, lineNum) ->
            Pair(
                instruction.copy(arguments = instruction.arguments.map {
                    if (it !is InstructionArgument.Label) return@map it
                    if (it.name == null) return@map it
                    return@map it.copy(value = labelsToRam[it.name] ?: -1)
                }),
                lineNum
            )
        }.forEach { (instruction, lineNum) ->
            val (instructionEnum, arguments) = instruction
            if (!instructionEnum.isValid(*arguments.toTypedArray())) {
                parseErrors.add(
                    Pair(
                        AsmError.ParseError.InvalidInstructionArgumentsFor(
                            instructionEnum,
                            arguments
                        ), lineNum
                    )
                )
                return@forEach
            }
            val instructionInstance = instructionEnum.create(*arguments.toTypedArray())
            memory.addAll(instructionInstance.toBytes().toList())
        }

        if (parseErrors.isNotEmpty()) return AsmResult.Failure(AsmError.ParseError.ParseErrors(parseErrors))
        return AsmResult.Success(Pair(memory.toByteArray(), labelsToRam.toMap()))
    }
}

data class LexedLine(val content: Content?, val comment: String?) {
    sealed interface Content {
        data class Instruction(val instruction: InstructionRewriteEnum, val arguments: List<InstructionArgument>) :
            Content

        data class Label(val name: String) : Content
        //+ directive related content for when directives are added
    }
}

sealed interface Lexeme {
    data class Identifier(val text: String) : Lexeme {
        override fun toString() = text
    }

    data class Comment(val text: String) : Lexeme {
        override fun toString() = "//$text"
    }

    data class Register(val register: RegisterEnum) : Lexeme {
        override fun toString() = register.name.lowercase()
    }

    data class Integer(val value: Int) : Lexeme {
        override fun toString() = value.toString()
    }

    data class FloatNum(val value: Float, val originalText: String? = null) : Lexeme {
        override fun toString() = originalText ?: value.toString()
    }

    data class Condition(val condition: ConditionEnum) : Lexeme {
        override fun toString() = condition.name.lowercase()
    }

    data class Mnemonic(val mnemonic: InstructionRewriteEnum) : Lexeme {
        override fun toString() = mnemonic.name.lowercase()
    }

    data class Error(val text: String) : Lexeme {
        override fun toString() = text
    }

    data object Comma : Lexeme {
        override fun toString() = ","
    }

    data object Colon : Lexeme {
        override fun toString() = ":"
    }

    data object Whitespace : Lexeme {
        override fun toString() = " "
    }

    data object LeftBracket : Lexeme {
        override fun toString() = "("
    }

    data object RightBracket : Lexeme {
        override fun toString() = ")"
    }
}
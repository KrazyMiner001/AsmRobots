package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.instructions.Instruction.Companion.byteLength
import krazyminer001.asmrobots.common.asm.instructions.InstructionEnum
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
            val byte = if (string.length == 4 && string[0] == '0' && string[1] == 'x') string.drop(2).toUByteOrNull(16) else null
            return when {
                null != int -> Lexeme.Integer(string)
                null != float -> Lexeme.FloatNum(string)
                null != condition -> Lexeme.Condition(condition)
                null != register -> Lexeme.Register(register)
                null != byte -> Lexeme.Byte(string.drop(2))
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
                            val mnemonic = InstructionEnum.entries.find { it.name.lowercase() == mainPart }
                            lexemes +=
                                if (mainPart == "emb")
                                    Lexeme.EmbedDirective
                                else
                                    mnemonic?.let { Lexeme.Mnemonic(it) } ?: parseArgument(mainPart)
                        }
                        lexemes += Lexeme.Whitespace
                        partialString = ""
                    }

                    partialString.endsWith("(") -> {
                        val mainPart = partialString.dropLast(1)
                        val int = mainPart.toIntOrNull()
                        lexemes += if (int == null) Lexeme.Error(mainPart) else Lexeme.Integer(mainPart)
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
                val mnemonic = InstructionEnum.entries.find { it.name.lowercase() == partialString }
                lexemes += mnemonic?.let { Lexeme.Mnemonic(it) } ?: parseArgument(partialString)
            }
            lexemes.toList()
        }
    }

    fun parse(line: List<Lexeme>): AsmResult<LexedLine, AsmError.ParseError> {
        if (line.any { it is Lexeme.Error })
            return AsmResult.Failure(AsmError.ParseError.UnparsableLine(line.joinToString("")))
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
                        is Lexeme.Integer -> arguments += InstructionArgument.Immediate32(lexeme.num)
                        is Lexeme.FloatNum -> arguments += InstructionArgument.ImmediateFloat32(lexeme.num)
                        else -> invalidArguments += lexeme.toString()
                    }
                } else if (argument.size == 4) {
                    val (offset, openBracket, register, closeBracket) = argument
                    if (offset is Lexeme.Integer && openBracket is Lexeme.LeftBracket && register is Lexeme.Register && closeBracket is Lexeme.RightBracket) {
                        arguments += InstructionArgument.Pointer(
                            register.register,
                            InstructionArgument.Immediate32(offset.num)
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

            if (!firstPart.mnemonic.isValid(*arguments.toTypedArray())) {
                return AsmResult.Failure(
                    AsmError
                        .ParseError
                        .InvalidInstructionArgumentsFor(firstPart.mnemonic, arguments)
                )
            }

            return LexedLine(LexedLine.Content.Instruction(firstPart.mnemonic, arguments), comment).asSuccess()
        }

        if (firstPart is Lexeme.EmbedDirective) {
            val bytes = parts
                .drop(1).let { if (comment != null) it.dropLast(1) else it }
                .chunked(2)
                .map {
                    val byte = it.getOrNull(0)
                    val comma = it.getOrNull(1)

                    if (comma != null && comma !is Lexeme.Comma) {
                        return AsmResult.Failure(AsmError.ParseError.InvalidDelimiter(comma.toString()))
                    }
                    if (byte !is Lexeme.Byte) {
                        return AsmResult.Failure(AsmError.ParseError.InvalidByte(byte.toString()))
                    }

                    byte.value
                }

            return LexedLine(LexedLine.Content.EmbedDirective(bytes), comment).asSuccess()
        }

        if (parts.size == 1) return LexedLine(null, comment).asSuccess()

        return AsmResult.Failure(AsmError.ParseError.UnparsableLine(line.joinToString("")))
    }

    fun assemble(lines: List<LexedLine>): Pair<ByteArray, Map<String, Int>> {
        val labels = mutableMapOf<String, Int>()
        val pendingLabels = mutableListOf<LexedLine.Content.Label>()
        val content = mutableListOf<Pair<LexedLine.Content, Int>>()
        lines.forEachIndexed { index, line ->
            when (line.content) {
                is LexedLine.Content.Label -> pendingLabels.add(line.content)
                is LexedLine.Content.Instruction, is LexedLine.Content.EmbedDirective -> {
                    labels.putAll(pendingLabels.map { Pair(it.name, content.size) })
                    pendingLabels.clear()
                    content.add(Pair(line.content, index))
                }

                else -> {}
            }
        }

        val instructionIndexToRam = mutableMapOf<Int, Int>()

        var memorySize = 0
        content.forEachIndexed { index, (content, _) ->
            instructionIndexToRam[index] = memorySize
            memorySize += when (content) {
                is LexedLine.Content.Instruction -> {
                    content.arguments.map { it.asEnum() }.byteLength() + 2
                }
                is LexedLine.Content.EmbedDirective -> {
                    content.values.size
                }
                else -> {
                    throw IllegalStateException("Content list has invalid object in it")
                }
            }
        }

        val memory = mutableListOf<Byte>()
        val labelsToRam = labels.mapValues { instructionIndexToRam[it.value]!! }

        content.map { (content, lineNum) ->
            if (content is LexedLine.Content.Instruction) {
                Pair(
                    content.copy(arguments = content.arguments.map {
                        if (it !is InstructionArgument.Label) return@map it
                        if (it.name == null) return@map it
                        return@map it.copy(value = labelsToRam[it.name] ?: -1)
                    }),
                    lineNum
                )
            } else {
                Pair(
                    content,
                    lineNum
                )
            }
        }.forEach { (content, lineNum) ->
            when (content) {
                is LexedLine.Content.EmbedDirective -> {
                    memory.addAll(content.values.map { it.toByte() })
                }
                is LexedLine.Content.Instruction -> {
                    val (instructionEnum, arguments) = content
                    val instructionInstance = instructionEnum.create(*arguments.toTypedArray())
                    memory.addAll(instructionInstance.toBytes().toList())
                }
                else -> throw IllegalStateException()
            }
        }

        return Pair(memory.toByteArray(), labelsToRam.toMap())
    }
}

data class LexedLine(val content: Content?, val comment: String?) {
    sealed interface Content {
        data class Instruction(val instruction: InstructionEnum, val arguments: List<InstructionArgument>) :
            Content

        data class Label(val name: String) : Content

        data class EmbedDirective(val values: List<UByte>) : Content
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

    data class Integer(val text: String) : Lexeme {
        override fun toString() = text
        val num = text.toInt()
    }

    data class FloatNum(val text: String) : Lexeme {
        override fun toString() = text
        val num = text.toFloat()
    }

    data class Byte(val text: String) : Lexeme {
        override fun toString() = text
        val value = text.toUByte(16)
    }

    data class Condition(val condition: ConditionEnum) : Lexeme {
        override fun toString() = condition.name.lowercase()
    }

    data class Mnemonic(val mnemonic: InstructionEnum) : Lexeme {
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

    data object EmbedDirective : Lexeme {
        override fun toString() = "emb"
    }
}
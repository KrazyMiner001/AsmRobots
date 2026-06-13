package krazyminer001.asmrobots.common.asm

import com.google.common.base.Splitter
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewrite.Companion.byteLength
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewriteEnum
import krazyminer001.asmrobots.common.asm.instructions.asEnum

fun lex(code: String): AsmResult<List<LexedLine>, AsmError.ParseError> {
    val lines = code.split('\n')

    return lines.map { line ->
        val trimmed = line.trim()
        val matches = CommentRegex.matchEntire(trimmed)!!.groups
        val comment = matches["comment"]?.value
        val content = matches["content"]!!.value.trim()
        if (content.endsWith(":")) {
            return@map LexedLine(LexedLine.Content.Label(content.dropLast(1)), comment)
        }
        if (content.isBlank()) {
            return@map LexedLine(null, comment)
        }

        val mnemonic = trimmed.substringBefore(" ")
        val components = Splitter.on(", ")
            .omitEmptyStrings()
            .split(trimmed.substringAfter(" ", ""))
            .toList()
            .map { Pair(InstructionArgument.parse(it), it) }
            .also { pairs ->
                val nulls = pairs.filter { it.first == null }
                if (nulls.isNotEmpty())
                    AsmResult.Failure(
                        AsmError
                            .ParseError
                            .InvalidInstructionArguments(*pairs.map { it.second }.toTypedArray())
                    )
            }
            .map { it.first }
            .filterIsInstance<InstructionArgument>()

        val instructionType = InstructionRewriteEnum.entries.find { it.name.lowercase() == mnemonic }
        if (instructionType == null)
            return AsmResult.Failure(AsmError.ParseError.InstructionNotFound(mnemonic))

        return@map LexedLine(LexedLine.Content.Instruction(instructionType, components), comment)
    }.asSuccess()
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

    if (parseErrors.isNotEmpty()) return AsmResult.Failure(AsmError.ParseError.ParseErrors(*parseErrors.toTypedArray()))
    return AsmResult.Success(Pair(memory.toByteArray(), labelsToRam.toMap()))
}

val CommentRegex: Regex = "^(?<content>.*?)(?://(?<comment>.*))?$".toRegex()

data class LexedLine(val content: Content?, val comment: String?) {
    sealed interface Content {
        data class Instruction(val instruction: InstructionRewriteEnum, val arguments: List<InstructionArgument>) : Content

        data class Label(val name: String) : Content
        //+ directive related content for when directives are added
    }
}
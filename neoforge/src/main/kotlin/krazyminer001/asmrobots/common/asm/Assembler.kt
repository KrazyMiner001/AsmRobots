package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.InstructionRewrite
import krazyminer001.asmrobots.common.asm.instructions.asEnum
import krazyminer001.asmrobots.common.asm.instructions.tryParse

fun lex(code: String): List<LexedLine> {
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
        return@map LexedLine(LexedLine.Content.Instruction(content), comment)
    }
}

fun assemble(lines: List<LexedLine>): AsmResult<Pair<ByteArray, Map<String, Int>>, AsmError.ParseError.ParseErrors> {
    val labels = mutableMapOf<String, Int>()
    val pendingLabels = mutableListOf<LexedLine.Content.Label>()
    val instructions = mutableListOf<LexedLine.Content.Instruction>()
    lines.forEach { line ->
        when (line.content) {
            is LexedLine.Content.Label -> pendingLabels.add(line.content)
            is LexedLine.Content.Instruction -> {
                labels.putAll(pendingLabels.map { Pair(it.name, instructions.size) })
                pendingLabels.clear()
                instructions.add(line.content)
            }
            else -> {}
        }
    }

    val parseErrors = mutableListOf<Pair<AsmError.ParseError, Int>>()

    val lineNumbersToRam = mutableMapOf<Int, Int>()

    val parsedInstructions = mutableListOf<InstructionRewrite>()
    var memorySize = 0
    instructions.forEachIndexed { index, instruction ->
        InstructionRewrite.tryParse(instruction.content, labels).fold(
            { parsedInstruction ->
                parsedInstructions.add(parsedInstruction)
                lineNumbersToRam[index] = memorySize
                memorySize += parsedInstruction.asEnum().toBytes(parsedInstruction).size
            },
            {
                parseErrors.add(Pair(it, index))
            }
        )
    }

    val memory = mutableListOf<Byte>()
    val labelsToRam = labels.mapValues { lineNumbersToRam[it.value]!! }

    instructions.forEachIndexed { index, instruction ->
        InstructionRewrite.tryParse(instruction.content, labelsToRam).fold(
            { parsedInstruction ->
                lineNumbersToRam[index] = memory.size
                memory.addAll(parsedInstruction.asEnum().toBytes(parsedInstruction).toTypedArray())
            },
            {
                parseErrors.add(Pair(it, index))
            }
        )
    }

    if (parseErrors.isNotEmpty()) return AsmResult.Failure(AsmError.ParseError.ParseErrors(*parseErrors.toTypedArray()))
    return AsmResult.Success(Pair(memory.toByteArray(), labelsToRam.toMap()))
}

val CommentRegex: Regex = "^(?<content>.*?)(?://(?<comment>.*))?$".toRegex()

data class LexedLine(val content: Content?, val comment: String?) {
    sealed interface Content {
        data class Instruction(val content: String) : Content
        data class Label(val name: String) : Content
        //+ directive related content for when directives are added
    }
}
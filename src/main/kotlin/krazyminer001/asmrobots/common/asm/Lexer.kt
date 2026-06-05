package krazyminer001.asmrobots.common.asm

import com.mojang.datafixers.util.Either

fun lex (code: String): List<AsmLine> {
    val lines = code.split('\n')
    return lines.map { line ->
        val trimmed = line.trim()
        val matches = CommentRegex.matchEntire(trimmed)!!.groups
        val comment = matches["comment"]?.value?.let(::Comment)
        val content = matches["content"]!!.value.trim()
        if (content.endsWith(":")) {
            return@map AsmLine(
                Either.right(Label(content.dropLast(1))),
                comment
            )
        }
        if (content.isBlank()) {
            return@map AsmLine(
                null,
                comment
            )
        }
        Instruction.tryParse(content).fold({
            return@map AsmLine(
                Either.left(it),
                comment
            )
        }) {
            throw it
        }
    }
}

val CommentRegex: Regex = "^(?<content>.*?)(?://(?<comment>.*))?$".toRegex()
package krazyminer001.asmrobots.common.asm

class Program {
    class Code {
        private val labels: MutableMap<Label, Int> = mutableMapOf()
        private val instructions: MutableList<Instruction> = mutableListOf()

        internal fun addLabel(label: Label) {
            labels[label] = instructions.size
        }

        internal fun addInstruction(instruction: Instruction) {
            instructions.add(instruction)
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
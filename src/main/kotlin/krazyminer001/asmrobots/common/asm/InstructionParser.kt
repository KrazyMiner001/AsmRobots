package krazyminer001.asmrobots.common.asm

enum class InstructionParser(private val function: (String) -> Any?) {
    REGISTER({ value -> Register.entries.find { it.name.equals(value, true) } }),
    LITERAL({ value -> value.toIntOrNull()?.let(::Literal) });

    operator fun invoke(value: String): Any? = function.invoke(value)
}
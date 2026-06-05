package krazyminer001.asmrobots.common.asm

@Target(AnnotationTarget.CLASS)
annotation class InstructionParameter(val displayName: String, val parser: InstructionParser)
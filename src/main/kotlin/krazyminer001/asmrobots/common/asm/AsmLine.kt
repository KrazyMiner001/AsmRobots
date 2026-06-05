package krazyminer001.asmrobots.common.asm

import com.mojang.datafixers.util.Either

data class AsmLine(val content: Either<Instruction, Label>?, val comment: Comment?)

data class Comment(val text: String)
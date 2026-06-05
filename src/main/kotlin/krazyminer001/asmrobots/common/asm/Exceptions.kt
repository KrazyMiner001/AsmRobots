package krazyminer001.asmrobots.common.asm

sealed interface InstructionException

class InstructionNotFoundException(instructionName: String)
    : NoSuchElementException("Could not find instruction $instructionName"), InstructionException

class InvalidInstructionParameterCount(instructionName: String, providedCount: Int, expectedCount: Int)
    : IllegalArgumentException("Instruction $instructionName expects $expectedCount parameters, but $providedCount were provided"), InstructionException

class InstructionInvalidParameter(parameterTypeName: String, providedValue: String)
    : IllegalArgumentException("Could not convert $providedValue to parameter type: $parameterTypeName"), InstructionException

class InternalInstructionException(message: String) : IllegalStateException(message), InstructionException
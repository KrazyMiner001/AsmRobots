package krazyminer001.asmrobots.common.asm

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

sealed interface Instruction {
    data class AddR(val target: Register, val arg1: Register) : Instruction
    data class AddRR(val target: Register, val arg1: Register, val arg2: Register) : Instruction
    data class AddI(val target: Register, val arg1: Literal) : Instruction
    data class AddRI(val target: Register, val arg1: Register, val arg2: Literal) : Instruction

    companion object {
        fun tryParse(code: String): Result<Instruction> {
            val mnemonic = code.substringBefore(" ")
            val components = code.substringAfter(" ").split(", ")
            val instructionConstructor = Instruction::class.sealedSubclasses
                .find { it.simpleName.equals(mnemonic, true) }
                ?.primaryConstructor ?: return Result.failure(InstructionNotFoundException(mnemonic))
            val parameters = instructionConstructor.parameters
            if (parameters.count() != components.count())
                return Result.failure(InvalidInstructionParameterCount(mnemonic, components.count(), parameters.count()))
            val args = components.zip(parameters).map { (component, parameter) ->
                val type = parameter.type
                val instructionParameter = (type.classifier as? KClass<*>)
                    ?.annotations
                    ?.find { it is InstructionParameter }
                    .let { it as? InstructionParameter ?: throw InternalInstructionException("Could not find InstructionParameter annotation for parameter $parameter for instruction $mnemonic") }
                val value = instructionParameter.parser(component)
                    ?: return Result.failure(InstructionInvalidParameter(
                            instructionParameter.displayName,
                            component))
                return@map Pair(parameter, value)
            }.toMap()
            return Result.success(instructionConstructor.callBy(args))
        }
    }
}

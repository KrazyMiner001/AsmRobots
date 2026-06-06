package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.annotations.Enumerated
import krazyminer001.asmrobots.common.asm.InstructionParser.LITERAL
import krazyminer001.asmrobots.common.asm.InstructionParser.REGISTER
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Enumerated(InstructionMember::class)
sealed interface Instruction {
    @InstructionMember(REGISTER, REGISTER)
    data class AddR(val target: Register, val arg1: Register) : Instruction
    @InstructionMember(REGISTER, REGISTER, REGISTER)
    data class AddRR(val target: Register, val arg1: Register, val arg2: Register) : Instruction
    @InstructionMember(REGISTER, LITERAL)
    data class AddI(val target: Register, val arg1: Literal) : Instruction
    @InstructionMember(REGISTER, REGISTER, LITERAL)
    data class AddRI(val target: Register, val arg1: Register, val arg2: Literal) : Instruction

    companion object {
        fun oldParser(code: String): Result<Instruction> {
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

        fun tryParse(code: String): Result<Instruction> {
            val mnemonic = code.substringBefore(" ")
            val components = code.substringAfter(" ").split(", ")
            val instruction = InstructionEnum.entries.find { it.name.lowercase() == mnemonic }

            if (instruction !is InstructionEnum) return Result.failure(InstructionNotFoundException(mnemonic))

            val parameters = instruction.InstructionMember.parameters.zip(components).map { (parser, string) ->
                parser(string)
            }
            return Result.success(instruction.creator.call(*parameters.toTypedArray()))
        }
    }
}
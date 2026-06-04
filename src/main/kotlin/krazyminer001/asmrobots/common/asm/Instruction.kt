package krazyminer001.asmrobots.common.asm

import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

sealed interface Instruction {
    data class AddR(val target: Register, val arg1: Register) : Instruction
    data class AddRR(val target: Register, val arg1: Register, val arg2: Register) : Instruction
    data class AddI(val target: Register, val arg1: Literal) : Instruction
    data class AddRI(val target: Register, val arg1: Register, val arg2: Literal) : Instruction

    companion object {
        fun tryParse(code: String): Instruction? {
            val mnemonic = code.substringBefore(" ")
            val components = code.substringAfter(" ").split(", ")
            val instructionConstructor = Instruction::class.sealedSubclasses
                .find { it.simpleName.equals(mnemonic, true) }
                ?.primaryConstructor ?: return null
            val parameters = instructionConstructor.parameters;
            if (parameters.count() != components.count()) return null
            val args = components.zip(parameters).map { (component, parameter) ->
                val type = parameter.type
                val value = ((type.classifier as? KClass<*>)?.companionObjectInstance as? AsmParsable<*>)
                    ?.parse(component) ?: return null
                return@map Pair(parameter, value)
            }.toMap()
            return try {
                instructionConstructor.callBy(args)
            } catch (_: Exception) {
                null
            }
        }
    }
}

interface AsmParsable<T> {
    fun parse(value: String): T?
}

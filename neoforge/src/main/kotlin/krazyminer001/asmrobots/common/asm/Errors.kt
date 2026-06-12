package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.instructions.InstructionRewriteEnum
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.contract

sealed interface AsmError {
    sealed interface ParseError : AsmError {
        data class InstructionNotFound(val instructionName: String) : ParseError {
            override val text = "Invalid instruction $instructionName"
        }

        data class InvalidInstructionArgumentsFor(val instruction: InstructionRewriteEnum, val providedArguments: List<InstructionArgument>) : ParseError {
            override val text = "Invalid arguments ($providedArguments) for instruction $instruction"
        }

        class InvalidInstructionArguments(vararg val arguments: String) : ParseError {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is InvalidInstructionArguments) return false

                if (!arguments.contentEquals(other.arguments)) return false

                return true
            }

            override fun hashCode(): Int {
                return arguments.contentHashCode()
            }

            override val text = "Could not parse instruction arguments: ${arguments.joinToString()}"
        }

        class ParseErrors(vararg errors: Pair<out ParseError, Int>) : ParseError {
            override val text: String = errors.joinToString("\n") { "Encountered error on line ${it.second}: ${it.first.text}" }
        }
    }

    sealed interface RuntimeError : AsmError {

    }

    val text: String
}

sealed class AsmResult<out T : Any, out E : AsmError> {
    data class Success<T : Any>(val value: T): AsmResult<T, Nothing>()
    data class Failure<E : AsmError>(val error: E) : AsmResult<Nothing, E>()

    @OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
    val successValue: T?
        get() {
            contract {
                returnsNotNull() implies (this@AsmResult is Success<T>)
                returns(null) implies (this@AsmResult is Failure<E>)
                (this@AsmResult is Success<T>) implies returnsNotNull()
            }
            return when(this) {
                is Failure<E> -> null
                is Success<T> -> this.value
            }
        }

    @OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
    val errorValue: E?
        get() {
            contract {
                returnsNotNull() implies (this@AsmResult is Failure<E>)
                returns(null) implies (this@AsmResult is Success<T>)
                (this@AsmResult is Failure<E>) implies returnsNotNull()
            }
            return when(this) {
                is Failure<E> -> this.error
                is Success<T> -> null
            }
        }

    @OptIn(ExperimentalContracts::class)
    val isSuccess: Boolean
        get() {
            contract {
                returns(false) implies (this@AsmResult is Failure<E>)
                returns(true) implies (this@AsmResult is Success<T>)
            }
            return when(this) {
                is Failure<*> -> false
                is Success<*> -> true
            }
        }

    @OptIn(ExperimentalContracts::class)
    val isFailure: Boolean
        get() {
            contract {
                returns(true) implies (this@AsmResult is Failure<E>)
                returns(false) implies (this@AsmResult is Success<T>)
            }
            return when(this) {
                is Failure<*> -> true
                is Success<*> -> false
            }
        }

    fun <R> fold(onSuccess: (T) -> R, onFailure: (E) -> R): R {
        if (isSuccess) return onSuccess(value)
        if (isFailure) return onFailure(error)
        throw IllegalStateException()
    }
}

fun <T : Any> T.asSuccess(): AsmResult.Success<T> = AsmResult.Success(this)

inline fun <T : Any, E : AsmError> AsmResult<T, E>.getOrElse(orElse: (E) -> T): T {
    if (isSuccess) return successValue!!
    if (isFailure) return orElse(errorValue!!)
    throw IllegalStateException()
}
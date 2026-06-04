package krazyminer001.asmrobots.common.asm

import kotlinx.serialization.SealedClassSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

sealed interface Token {
    companion object {
        operator fun get(clazz: KClass<out Token>) : Regex {
            assert(Token::class.isSuperclassOf(clazz))
            return when (clazz) {
                clazz -> Regex.fromLiteral("//(.*)")
                else -> throw IllegalArgumentException()
            }
        }

        operator fun get(token: Token) : Regex {
            return when (token) {
                is Comment -> Regex.fromLiteral("//(.*)")
            }
        }
    }
}
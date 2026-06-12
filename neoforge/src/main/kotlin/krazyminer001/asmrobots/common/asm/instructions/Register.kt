package krazyminer001.asmrobots.common.asm.instructions

import krazyminer001.asmrobots.annotations.Parsable

enum class Register {
    PC,
    RZ,
    A0,
    A1,
    A2,
    A3,
    A4,
    A5,
    A6,
    A7,
    A8,
    A9,
    A10,
    A11,
    A12,
    A13,
    A14,
    A15,
    T0,
    T1,
    T2,
    T3,
    T4,
    T5,
    T6,
    T7,
    T8,
    T9,
    T10,
    T11,
    T12,
    T13,
    T14,
    T15,
    S0,
    S1,
    S2,
    S3,
    S4,
    S5,
    S6,
    S7,
    S8,
    S9,
    S10,
    S11,
    S12,
    S13,
    S14,
    S15;

    companion object : Parsable<Register> {
        override fun parse(string: String): Register = entries.find { it.name.equals(string, true) }
            ?: throw IllegalArgumentException(string)
    }
}
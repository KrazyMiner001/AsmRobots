package krazyminer001.asmrobots.common.asm

import krazyminer001.asmrobots.common.asm.instructions.Register

class RegisterStorage {
    private val array = Array(Register.entries.size) { 0 }

    operator fun get(register: Register): Int {
        if (register == Register.RZ) return 0
        return array[register.ordinal]
    }

    operator fun set(register: Register, value: Int) {
        if (register == Register.RZ) return
        array[register.ordinal] = value
    }
}
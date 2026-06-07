package krazyminer001.asmrobots.common.asm

class RegisterStorage {
    private val array = Array(Register.entries.size) { 0 }

    operator fun get(register: Register): Int {
        return array[register.ordinal]
    }

    operator fun set(register: Register, value: Int) {
        array[register.ordinal] = value
    }

    var Register.value: Int
        get() = get(this)
        set(value) = set(this, value)
}
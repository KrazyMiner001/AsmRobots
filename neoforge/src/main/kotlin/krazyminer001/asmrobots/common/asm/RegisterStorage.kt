package krazyminer001.asmrobots.common.asm

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

    var Register.value: Int
        get() = get(this)
        set(value) = set(this, value)

    val Pointer.value: Int
        get() = this.register.value + this.offset.value
}
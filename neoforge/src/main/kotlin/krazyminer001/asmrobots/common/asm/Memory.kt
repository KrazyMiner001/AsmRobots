package krazyminer001.asmrobots.common.asm

import kotlin.math.min

class Memory(size: Int, val shouldApplyMaps: () -> Boolean) {
    val realMemory: ByteArray = ByteArray(size) { 0 }
    val memoryMaps: MutableMap<MemoryMapId, MemoryMap> = mutableMapOf()

    fun initRealMemory(memory: ByteArray) {
        memory.copyInto(realMemory, endIndex = min(realMemory.size, memory.size))
    }

    fun createMemoryMap(
        startAddress: Int,
        size: Int,
        getter: (Int) -> Byte,
        setter: (Int, Byte) -> Unit,
    ) : MemoryMapId {
        val range = (startAddress..<startAddress+size)

        if (memoryMaps.values.any { it.range.intersect(range).isNotEmpty() }) {
            return MemoryMapId.ERROR
        }

        val memoryMap = MemoryMap(startAddress, size, getter, setter)
        val id = MemoryMapId((memoryMaps.keys.maxOfOrNull { it.id } ?: 0) + 1)

        memoryMaps[id] = memoryMap
        return id
    }

    fun removeMemoryMap(id: MemoryMapId) {
        memoryMaps.remove(id)
    }

    fun copyOfRange(fromIndex: Int, toIndex: Int): ByteArray {
        return (fromIndex..<toIndex)
            .map { this[it] }
            .toByteArray()
    }

    operator fun get(index: Int): Byte {
        if (!shouldApplyMaps()) { return realMemory[index] }

        return memoryMaps
            .filter { index in it.value }
            .maxByOrNull { it.key.id }
            ?.value
            ?.get(index) ?: realMemory[index]
    }

    operator fun set(index: Int, value: Byte) {
        val map = memoryMaps
            .filter { index in it.value }
            .maxByOrNull { it.key.id }
            ?.value

        if (map == null || !shouldApplyMaps()) {
            realMemory[index] = value
        } else {
            map[index] = value
        }
    }

    @JvmInline
    value class MemoryMapId(val id: Int) {
        companion object {
            val ERROR = MemoryMapId(-1)
        }
    }

    data class MemoryMap(
        val startAddress: Int,
        val size: Int,
        private val getter: (Int) -> Byte,
        private val setter: (Int, Byte) -> Unit
    ) {
        val range = startAddress..<startAddress+size

        operator fun get(index: Int) : Byte {
            require(index in this)
            return getter(index - startAddress)
        }

        operator fun set(index: Int, value: Byte) {
            require(index in this)
            setter(index - startAddress, value)
        }

        operator fun contains(index: Int): Boolean = index in range
    }
}
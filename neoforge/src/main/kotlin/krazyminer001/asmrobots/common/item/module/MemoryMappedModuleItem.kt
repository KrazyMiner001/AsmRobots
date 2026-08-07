package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.world.item.ItemStack

abstract class MemoryMappedModuleItem(properties: Properties) : ModuleItem(properties) {
    abstract fun getMappedMemory(identifier: Int, address: Int, itemStack: ItemStack, robotEntity: RobotEntity): Byte
    abstract fun setMappedMemory(
        identifier: Int,
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Byte
    )

    fun getMap(identifier: Int, itemStack: ItemStack, robotEntity: RobotEntity) =
        MappedMemory(identifier, itemStack, robotEntity)

    inner class MappedMemory(val identifier: Int, val itemStack: ItemStack, val robotEntity: RobotEntity) {
        operator fun get(address: Int): Byte = getMappedMemory(identifier, address, itemStack, robotEntity)
        operator fun set(address: Int, value: Byte) =
            setMappedMemory(identifier, address, itemStack, robotEntity, value)
    }
}
package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import net.minecraft.world.item.ItemStack

class SolidStateDriveModule(properties: Properties) : MemoryMappedModuleItem(properties) {
    override fun getMappedMemory(
        identifier: Int,
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Byte {
        return itemStack
            .get(ModComponents.SOLID_STATE_DRIVE)
            ?.toMutableList()
            ?.getOrNull(address + identifier)
            ?: 0
    }

    override fun setMappedMemory(
        identifier: Int,
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Byte
    ) {
        val component = itemStack.get(ModComponents.SOLID_STATE_DRIVE)?.toMutableList() ?: return
        runCatching { component[address + identifier] = value }
        itemStack.set(ModComponents.SOLID_STATE_DRIVE, component)
    }

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.SIZE -> itemStack.get(ModComponents.SOLID_STATE_DRIVE)?.size ?: 0
            else -> 0
        }
    }

    override fun setIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Int
    ) {
        // nothing to set
    }

    object IOPorts {
        const val SIZE = 0
    }
}
package krazyminer001.asmrobots.common.item.modules

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import net.minecraft.world.item.ItemStack

class HardDriveModule(properties: Properties) : ModuleItem(properties) {
    var address = 0

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            0 -> this.address
            1 -> itemStack.get(ModComponents.HARD_DRIVE)?.getOrNull(address)?.toUInt()?.toInt() ?: 0
            else -> 0
        }
    }

    override fun setIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Int
    ) {

        when (address) {
            0 -> this.address = value
            1 -> {
                val component = itemStack.get(ModComponents.HARD_DRIVE)?.toMutableList() ?: return
                runCatching { component[address] = value.toByte() }
                itemStack.set(ModComponents.HARD_DRIVE, component)
            }
        }
    }
}
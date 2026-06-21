package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class ModuleItem(properties: Properties) : Item(properties) {
    abstract fun getIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity): Int
    abstract fun setIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity, value: Int)
}
package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.asm.Program
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class ModuleItem(properties: Properties) : Item(properties) {
    abstract fun getIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity): Int
    abstract fun setIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity, value: Int)
    open fun tick(program: Program, portOffset: Int, level: ServerLevel) = Unit
}
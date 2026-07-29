package krazyminer001.asmrobots.common.item.upgrade

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.world.item.ItemStack

class ProcessingSpeedUpgrade(properties: Properties) : UpgradeItem(properties) {
    override fun tick(stack: ItemStack, robot: RobotEntity) {
        robot.stepProgram()
    }
}
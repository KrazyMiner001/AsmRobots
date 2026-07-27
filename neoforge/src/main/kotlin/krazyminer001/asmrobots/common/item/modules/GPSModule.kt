package krazyminer001.asmrobots.common.item.modules

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.world.item.ItemStack

class GPSModule(properties: Properties) : ModuleItem(properties) {
    override fun getIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity): Int {
        return when (address) {
            IOPorts.X -> robotEntity.blockX
            IOPorts.Y -> robotEntity.blockY
            IOPorts.Z -> robotEntity.blockZ
            else -> 0
        }
    }

    override fun setIOPort(address: Int, itemStack: ItemStack, robotEntity: RobotEntity, value: Int) = Unit

    object IOPorts {
        const val X = 0
        const val Y = 1
        const val Z = 2
    }
}
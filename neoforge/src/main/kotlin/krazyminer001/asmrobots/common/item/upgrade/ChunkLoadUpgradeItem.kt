package krazyminer001.asmrobots.common.item.upgrade

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.world.ModTicketTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack

class ChunkLoadUpgradeItem(properties: Properties) : UpgradeItem(properties) {
    override fun tick(stack: ItemStack, robot: RobotEntity) {
        val level = robot.level()
        if (level !is ServerLevel) return

        val chunk = robot.chunkPosition()
        level.chunkSource.addTicketWithRadius(
            ModTicketTypes.ROBOT_LOADER,
            chunk,
            1
        )
    }
}
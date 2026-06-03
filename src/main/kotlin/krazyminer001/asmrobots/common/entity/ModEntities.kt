package krazyminer001.asmrobots.common.entity

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModEntities {
    val REGISTRY = DeferredRegister.createEntities(AsmRobots.ID)

    val ROBOT_ENTITY by REGISTRY.registerEntityType(
        "robot",
        ::RobotEntity,
        MobCategory.MISC)
}
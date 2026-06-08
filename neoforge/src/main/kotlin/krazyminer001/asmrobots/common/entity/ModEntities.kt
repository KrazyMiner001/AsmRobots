package krazyminer001.asmrobots.common.entity

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModEntities {
    val REGISTRY = DeferredRegister.createEntities(AsmRobots.ID)

    val ROBOT_ENTITY by REGISTRY.registerEntityType(
        "robot",
        ::RobotEntity,
        MobCategory.MISC) {
        it
            .sized(12f/16, 13f/16)
    }
}
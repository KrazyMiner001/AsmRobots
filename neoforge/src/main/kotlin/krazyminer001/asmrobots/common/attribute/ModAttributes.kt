package krazyminer001.asmrobots.common.attribute

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.world.entity.ai.attributes.AttributeModifier

object ModAttributes {
    val SPEED_UPGRADE_ATTRIBUTE_MODIFIER = AttributeModifier(
        AsmRobots.namespacedIdentifier("speed_upgrade_modifier"),
        0.25,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )

    val STEP_HEIGHT_UPGRADE_ATTRIBUTE_MODIFIER = AttributeModifier(
        AsmRobots.namespacedIdentifier("step_height_upgrade_modifier"),
        0.5,
        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    )
}
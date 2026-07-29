package krazyminer001.asmrobots.common.item.upgrade

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class UpgradeItem(properties: Properties) : Item(properties) {
    open val attributeModifiers: Array<out Pair<Holder<Attribute>, AttributeModifier>> = arrayOf()
    open fun tick(stack: ItemStack, robot: RobotEntity) = Unit
}
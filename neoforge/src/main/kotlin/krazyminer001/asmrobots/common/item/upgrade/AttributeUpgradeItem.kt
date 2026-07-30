package krazyminer001.asmrobots.common.item.upgrade

import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier

class AttributeUpgradeItem(
    properties: Properties,
    override vararg val attributeModifiers: Pair<Holder<Attribute>, AttributeModifier>
) : UpgradeItem(properties) {

    companion object {
        fun withModifiers(
            vararg attributeModifiers: Pair<Holder<Attribute>, AttributeModifier>
        ) = { properties: Properties -> AttributeUpgradeItem(properties, *attributeModifiers) }
    }
}
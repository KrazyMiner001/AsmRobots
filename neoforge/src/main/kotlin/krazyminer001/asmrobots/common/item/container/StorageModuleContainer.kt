package krazyminer001.asmrobots.common.item.container

import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.component.StorageModuleComponent
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack

class StorageModuleContainer(private val stack: ItemStack) :
    SimpleContainer(stack.get(ModComponents.STORAGE_COMPONENT)?.size ?: 0) {
    init {
        val contents = stack.get(ModComponents.STORAGE_COMPONENT) ?: StorageModuleComponent(size = 0)
        contents.itemContainerComponent.copyInto(items)
    }

    override fun setChanged() {
        super.setChanged()
        stack.set(ModComponents.STORAGE_COMPONENT, StorageModuleComponent.fromItems(items))
    }
}
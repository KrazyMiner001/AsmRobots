package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class RobotCraftInput(val robot: RobotEntity) : RecipeInput {
    override fun getItem(index: Int): ItemStack {
        var remainingIndex = index
        var stack = ItemStack.EMPTY
        for ((component, size) in robot.modulesInventory.items
            .mapNotNull {
                it.components.get(ModComponents.STORAGE_COMPONENT)
            }) {

            if (size > remainingIndex) {
                stack = component.getStackInSlot(remainingIndex)
                break
            } else {
                remainingIndex -= size
            }
        }

        return stack
    }

    override fun size(): Int {
        return robot.modulesInventory.items.mapNotNull { stack ->
            stack.components.get(ModComponents.STORAGE_COMPONENT)?.size
        }.reduce(Int::plus)
    }
}
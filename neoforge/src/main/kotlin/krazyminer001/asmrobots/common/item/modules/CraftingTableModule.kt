package krazyminer001.asmrobots.common.item.modules

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.RecipeCache
import net.minecraft.world.level.Level
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.Transaction
import kotlin.jvm.optionals.getOrNull

class CraftingTableModule(properties: Properties) : ModuleItem(properties) {
    var index = 0
    var crafted = -1

    fun getItemAtIndex(index: Int, stack: ItemStack, level: Level): ItemStack {
        if (level !is ServerLevel) return ItemStack.EMPTY
        if (stack.item !is CraftingTableModule) return ItemStack.EMPTY
        val storageComponent = stack.get(ModComponents.STORAGE_COMPONENT) ?: return ItemStack.EMPTY

        if (storageComponent.size != 10) return ItemStack.EMPTY

        if (index in 0..9) return storageComponent.itemContainerComponent.getStackInSlot(index)

        return ItemStack.EMPTY
    }

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.INDEX -> index

            IOPorts.COUNT -> getItemAtIndex(index, itemStack, robotEntity.level() as ServerLevel).count

            IOPorts.ITEM -> BuiltInRegistries.ITEM.getId(getItemAtIndex(index, itemStack, robotEntity.level()).item)

            IOPorts.CRAFT -> crafted

            else -> 0
        }
    }

    override fun setIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Int
    ) {
        when (address) {
            IOPorts.INDEX -> index = value
            IOPorts.CRAFT -> {
                val level = robotEntity.level()
                if (level !is ServerLevel) return

                val excessItemsResourceHandler = ItemStacksResourceHandler(9)

                Transaction.openRoot().use { tx ->
                    val storageComponent = itemStack.get(ModComponents.STORAGE_COMPONENT) ?: return@use

                    val resourceHandler = VanillaContainerWrapper.of(StorageModuleContainer(itemStack))

                    val craftingInput = CraftingInput.of(
                        3,
                        3,
                        storageComponent.itemContainerComponent.allItemsCopyStream().toList()
                    )
                    val recipe = RECIPE_CACHE.get(
                        level,
                        craftingInput
                    ).getOrNull()?.value ?: return@use

                    val item = ItemResource.of(recipe.assemble(craftingInput))
                    if (item.isEmpty) return@use

                    recipe.getRemainingItems(craftingInput).forEach {
                        if (it.isEmpty) return@forEach

                        val inserted = excessItemsResourceHandler
                            .insert(ItemResource.of(it), 1, tx)

                        if (inserted != 1) return@use
                    }

                    for (i in 0..8) {
                        val resource = resourceHandler.getResource(i)
                        if (resource.isEmpty) continue
                        val extracted = resourceHandler.extract(i, resource, 1, tx)
                        if (extracted != 1) return@use
                    }

                    val inserted = resourceHandler.insert(9, item, 1, tx)
                    if (inserted != 1) return
                    tx.commit()
                }

                excessItemsResourceHandler.copyToList().forEach {
                    robotEntity.drop(it, true, false)
                }
            }
        }
    }

    object IOPorts {
        const val INDEX = 0
        const val COUNT = 1
        const val ITEM = 2
        const val CRAFT = 3
    }

    companion object {
        private val RECIPE_CACHE = RecipeCache(10)
    }
}

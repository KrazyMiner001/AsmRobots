package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.neoforge.transfer.CombinedResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.Transaction
import kotlin.streams.asSequence

object RobotRecipeCrafter {
    fun identifyRecipes(robot: RobotEntity, level: ServerLevel): List<RecipeHolder<RobotCraftRecipe>> {
        return level.recipeAccess().recipeMap().getRecipesFor(
            ModRecipeTypes.ROBOT_CRAFT,
            RobotCraftInput(robot),
            level
        ).asSequence().toList()
    }

    fun craft(robot: RobotEntity, recipe: RobotCraftRecipe): ItemStack {
        val input = RobotCraftInput(robot)
        val requiredInput = recipe.calculateInputs(input) ?: return ItemStack.EMPTY

        val resourceHandler = robot
            .modulesInventory
            .items
            .map { VanillaContainerWrapper.of(StorageModuleContainer(it)) }
            .filter { it.size() > 0 }
            .let { CombinedResourceHandler(*it.toTypedArray()) }

        val remainingToInsert: Int
        val output = recipe.assemble(input)

        Transaction.openRoot().use { tx ->
            requiredInput.forEach { (item, count) ->
                val resource = ItemResource.of(item)
                val extracted = resourceHandler.extract(resource, count, tx)
                if (extracted != count) {
                    return ItemStack.EMPTY
                }
            }

            val inserted = resourceHandler
                .insert(ItemResource.of(output), output.count, tx)

            remainingToInsert = output.count - inserted

            tx.commit()
        }

        robot.drop(output.copyWithCount(remainingToInsert), true, false)

        return output
    }
}
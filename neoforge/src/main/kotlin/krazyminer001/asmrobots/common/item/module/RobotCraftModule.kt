package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipe
import krazyminer001.asmrobots.common.recipe.RobotRecipeCrafter
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.server.ServerLifecycleHooks

class RobotCraftModule(properties: Properties) : ModuleItem(properties) {
    var recipeIndex = 0
    var recipeInputIndex = 0
    var recipeIngredientIndex = 0
    var lastCraftedItemsCount = -1

    fun getRecipes(robotEntity: RobotEntity): List<RobotCraftRecipe> {
        val level = robotEntity.level() as? ServerLevel ?: ServerLifecycleHooks.getCurrentServer()
            ?.getLevel(robotEntity.level().dimension()) ?: return listOf()

        return RobotRecipeCrafter.identifyRecipes(robotEntity, level).map { it.value }
    }

    override fun getIOPort(
        address: Int, itemStack: ItemStack, robotEntity: RobotEntity
    ): Int {
        val port = IOPorts.entries.getOrNull(address)
        return when (port) {
            IOPorts.RECIPES_INDEX -> recipeIndex
            IOPorts.SELECTED_RECIPE_OUTPUT_ITEM -> getRecipes(robotEntity)[recipeIndex]
                .result.item().value().let { BuiltInRegistries.ITEM.getId(it) }

            IOPorts.SELECTED_RECIPE_OUTPUT_COUNT -> getRecipes(robotEntity)[recipeIndex]
                .result.count

            IOPorts.RECIPE_INPUT_INDEX -> recipeInputIndex
            IOPorts.RECIPE_INPUT_INGREDIENT_INDEX -> recipeIngredientIndex
            IOPorts.RECIPE_INPUT_ITEM -> getRecipes(robotEntity)[recipeIndex]
                .items
                .keys
                .toList()[recipeInputIndex]
                .values[recipeIngredientIndex]
                .value()
                .let { BuiltInRegistries.ITEM.getId(it) }

            IOPorts.RECIPE_INPUT_COUNT -> getRecipes(robotEntity)[recipeIndex]
                .items
                .values
                .toList()[recipeInputIndex]

            IOPorts.CRAFT -> lastCraftedItemsCount
            else -> 0
        }
    }

    override fun setIOPort(
        address: Int, itemStack: ItemStack, robotEntity: RobotEntity, value: Int
    ) {
        val port = IOPorts.entries.getOrNull(address)
        when (port) {
            IOPorts.RECIPES_INDEX -> recipeIndex = value
            IOPorts.RECIPE_INPUT_INDEX -> recipeInputIndex = value
            IOPorts.RECIPE_INPUT_INGREDIENT_INDEX -> recipeIngredientIndex = value
            IOPorts.CRAFT -> {
                val recipes = getRecipes(robotEntity)
                recipes.getOrNull(recipeIndex)?.let { recipe ->
                    val craftResult = RobotRecipeCrafter.craft(robotEntity, recipe)
                    lastCraftedItemsCount = craftResult.count
                } ?: { lastCraftedItemsCount = -1 }
            }

            else -> {}
        }
    }

    enum class IOPorts {
        RECIPES_INDEX,
        SELECTED_RECIPE_OUTPUT_ITEM,
        SELECTED_RECIPE_OUTPUT_COUNT,
        RECIPE_INPUT_INDEX,
        RECIPE_INPUT_INGREDIENT_INDEX,
        RECIPE_INPUT_ITEM,
        RECIPE_INPUT_COUNT,
        CRAFT,
    }
}
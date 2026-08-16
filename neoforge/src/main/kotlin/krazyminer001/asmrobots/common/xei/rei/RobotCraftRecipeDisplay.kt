package krazyminer001.asmrobots.common.xei.rei

import com.mojang.serialization.MapCodec
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.util.EntryIngredients

class RobotCraftRecipeDisplay(val recipe: RobotCraftRecipe)
    : BasicDisplay(
        recipe.items.map { (ingredient, count) -> EntryIngredients.ofIngredient(ingredient) },
        listOf(EntryIngredients.of(recipe.result.create()))
    ) {
    override fun getCategoryIdentifier(): CategoryIdentifier<RobotCraftRecipeDisplay> = RobotCraftRecipeCategory.IDENTIFIER

    override fun getSerializer(): DisplaySerializer<RobotCraftRecipeDisplay> = DisplaySerializer.of(MAP_CODEC, STREAM_CODEC)

    companion object {
        val MAP_CODEC: MapCodec<RobotCraftRecipeDisplay> = RobotCraftRecipe.CODEC.xmap(
            ::RobotCraftRecipeDisplay,
            RobotCraftRecipeDisplay::recipe
        )

        val STREAM_CODEC = RobotCraftRecipe.STREAM_CODEC.map(
            ::RobotCraftRecipeDisplay,
            RobotCraftRecipeDisplay::recipe
        )
    }
}
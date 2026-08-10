package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.recipe.ModRecipeBookCategories.RobotBookCategory
import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderSet
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike

class RobotCraftRecipeBuilder(val result: ItemStackTemplate, val category: RecipeCategory) : RecipeBuilder {

    val advancementBuilder: RecipeUnlockAdvancementBuilder = RecipeUnlockAdvancementBuilder()
    var group: String = ""
    val items: MutableMap<Ingredient, Int> = mutableMapOf()
    var showNotification: Boolean = false
    var bookCategory: RobotBookCategory = RobotBookCategory.MISC

    override fun unlockedBy(
        name: String,
        criterion: Criterion<*>
    ): RobotCraftRecipeBuilder {
        this.advancementBuilder.unlockedBy(name, criterion)
        return this
    }

    override fun group(group: String?): RobotCraftRecipeBuilder {
        this.group = group ?: ""
        return this
    }

    fun showNotification(showNotification: Boolean): RobotCraftRecipeBuilder {
        this.showNotification = showNotification
        return this
    }

    fun item(item: Pair<Ingredient, Int>): RobotCraftRecipeBuilder {
        items.compute(item.first) { _, value ->
            (value ?: 0) + item.second
        }

        return this
    }

    fun item(item: HolderSet<Item>, count: Int = 1) = item(Ingredient.of(item), count)

    fun item(item: Ingredient, count: Int = 1) = item(Pair(item, count))

    fun item(item: ItemLike, count: Int = 1) = item(Ingredient.of(item), count)

    fun items(vararg items: Pair<Ingredient, Int>): RobotCraftRecipeBuilder {
        items.forEach { this.item(it) }

        return this
    }

    fun bookCategory(bookCategory: RobotBookCategory): RobotCraftRecipeBuilder {
        this.bookCategory = bookCategory

        return this
    }

    override fun defaultId() = RecipeBuilder.getDefaultRecipeId(result)

    override fun save(
        output: RecipeOutput,
        location: ResourceKey<Recipe<*>>
    ) {
        val recipe = RobotCraftRecipe(
            result,
            items,
            Recipe.CommonInfo(showNotification),
            bookCategory,
            group
        )

        output.accept(location, recipe, this.advancementBuilder.build(output, location, category))
    }
}
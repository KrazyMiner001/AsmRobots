package krazyminer001.asmrobots.common.xei.jei

import com.lowdragmc.lowdraglib2.integration.xei.jei.ModularUIRecipeCategory
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipe
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IJeiHelpers
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.network.chat.Component

class RobotCraftRecipeCategory(helpers: IJeiHelpers) :
    ModularUIRecipeCategory<RobotCraftRecipe>(RobotCraftRecipe::createJEIModularUI) {

    private val icon: IDrawable = helpers.guiHelper.createDrawableItemLike(ModItems.ROBOT)

    override fun getWidth(): Int {
        return RobotCraftRecipe.WIDTH
    }

    override fun getHeight(): Int {
        return RobotCraftRecipe.HEIGHT
    }

    override fun getRecipeType() = TYPE

    override fun getTitle() = Component.translatable("category.asmrobots.robot_craft")

    override fun getIcon() = icon

    companion object {
        val TYPE = IRecipeType.create(
            AsmRobots.namespacedIdentifier("robot_craft"),
            RobotCraftRecipe::class.java
        )
    }
}
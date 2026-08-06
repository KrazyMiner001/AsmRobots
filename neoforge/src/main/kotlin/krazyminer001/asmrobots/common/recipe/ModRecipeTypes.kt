package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModRecipeTypes {
    val REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, AsmRobots.ID)

    val ROBOT_CRAFT: RecipeType<RobotCraftRecipe> by REGISTRY.register(
        "robot_craft",
        RecipeType<*>::simple
    )
}
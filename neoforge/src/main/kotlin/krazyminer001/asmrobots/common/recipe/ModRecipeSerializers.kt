package krazyminer001.asmrobots.common.recipe

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModRecipeSerializers {
    val REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AsmRobots.ID)

    val ROBOT_CRAFT by REGISTRY.register("robot_craft") { ->
        RecipeSerializer(RobotCraftRecipe.CODEC, RobotCraftRecipe.STREAM_CODEC)
    }
}
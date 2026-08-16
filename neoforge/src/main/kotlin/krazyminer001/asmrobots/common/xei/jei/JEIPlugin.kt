package krazyminer001.asmrobots.common.xei.jei

import krazyminer001.asmrobots.client.AsmRobotsClient
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.recipe.ModRecipeTypes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.server.ServerLifecycleHooks

@JeiPlugin
class JEIPlugin : IModPlugin {
    override fun getPluginUid() = Identifier.fromNamespaceAndPath(AsmRobots.ID, "jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(
            RobotCraftRecipeCategory(registration.jeiHelpers)
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        val recipes = when (FMLEnvironment.getDist()) {
            Dist.CLIENT -> AsmRobotsClient.ROBOT_RECIPES
            Dist.DEDICATED_SERVER -> ServerLifecycleHooks
                .getCurrentServer()!!
                .recipeManager.recipeMap()
                .byType(ModRecipeTypes.ROBOT_CRAFT)
        }

        registration.addRecipes(
            RobotCraftRecipeCategory.TYPE,
            recipes.map { it.value }
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addCraftingStation(
            RobotCraftRecipeCategory.TYPE,
            ModItems.ROBOT_CRAFT_MODULE
        )
    }
}
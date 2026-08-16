package krazyminer001.asmrobots.common.xei.rei

import krazyminer001.asmrobots.client.AsmRobotsClient
import krazyminer001.asmrobots.common.item.ModItems
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.forge.REIPluginClient

@REIPluginClient
class ModREIClientPlugin : REIClientPlugin {
    override fun registerDisplays(registry: DisplayRegistry) {
        AsmRobotsClient.ROBOT_RECIPES.forEach { registry.add(RobotCraftRecipeDisplay(it.value)) }
    }

    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(RobotCraftRecipeCategory())
        registry.addWorkstations(RobotCraftRecipeCategory.IDENTIFIER, EntryStacks.of(ModItems.ROBOT_CRAFT_MODULE))
    }
}
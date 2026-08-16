package krazyminer001.asmrobots.common.xei.rei

import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEventDispatcher
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipe
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.network.chat.Component

class RobotCraftRecipeCategory : DisplayCategory<RobotCraftRecipeDisplay> {

    override fun getCategoryIdentifier(): CategoryIdentifier<RobotCraftRecipeDisplay> = IDENTIFIER

    override fun getTitle() = Component.translatable("category.asmrobots.robot_craft")

    override fun getIcon(): Renderer = EntryStacks.of(ModItems.ROBOT)

    override fun getDisplayHeight() = RobotCraftRecipe.HEIGHT
    override fun getDisplayWidth(display: RobotCraftRecipeDisplay): Int = RobotCraftRecipe.WIDTH

    override fun setupDisplay(display: RobotCraftRecipeDisplay, bounds: Rectangle): List<Widget> {
        val list = mutableListOf<Widget>()

        val modularUI = display.recipe.createREIModularUI()
        list.add(
            ModularREIWidget(
                modularUI
                    .apply {
                        isDrawTooltips = false
                        init(getDisplayWidth(display), displayHeight)
                    },
                bounds
            )
        )

        val event = UIEvent.create(ModularUIREIHelper.REI_WIDGET_EVENT).apply {
            target = modularUI.ui.rootElement
            customData = ModularUIREIHelper.ReiSlotWidgetHolder(list)
        }
        UIEventDispatcher.dispatchAllChildren(event)

        return list
    }

    companion object {
        val IDENTIFIER: CategoryIdentifier<RobotCraftRecipeDisplay> = CategoryIdentifier.of(AsmRobots.namespacedIdentifier("robot_craft_jei_category"))
    }
}
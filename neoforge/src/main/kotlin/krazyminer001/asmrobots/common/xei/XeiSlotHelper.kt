package krazyminer001.asmrobots.common.xei

import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlotElement
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import krazyminer001.asmrobots.common.xei.rei.ModularUIREIHelper.reiWidget
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import java.util.stream.Stream

object XeiSlotHelper {
    context(provider: XeiProvider)
    fun ItemSlotElement<ItemSlot>.xeiSlotWidget(ingredientIO: IngredientIO = IngredientIO.NONE, ingredient: Ingredient? = null, count: Int? = null) {
        val count = count ?: this.element.slot.item.count

        when (provider) {
            XeiProvider.JEI -> {
                element.xeiRecipeIngredient(ingredientIO) {
                    ingredient?.values?.stream()?.map { ItemStack(it, count) }
                        ?: Stream.of(this.element.slot.item)
                }

                element.xeiRecipeSlot(
                    IngredientIO.NONE,
                    1f,
                    count
                ) {
                    ingredient?.values?.stream()?.map { ItemStack(it, count) }
                        ?: Stream.of(this.element.slot.item)
                }
            }
            XeiProvider.REI -> {
                reiWidget(ingredientIO, ingredient?.values)
            }
        }
    }
}
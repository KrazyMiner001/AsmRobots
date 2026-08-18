package krazyminer001.asmrobots.common.xei.rei

import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlotElement
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import me.shedaniel.math.Point
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics
import me.shedaniel.rei.api.client.gui.widgets.Slot
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget
import net.minecraft.core.HolderSet
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.joml.Matrix3x2f
import org.joml.Vector2f

object ModularUIREIHelper {
    fun wrapSlot(slot: ItemSlot, ingredientIO: IngredientIO = IngredientIO.NONE, options: HolderSet<out ItemLike>? = null, count: Int = 1): Slot {
        return InvisibleEntryWidget(Point(slot.positionX.toDouble(), slot.positionY.toDouble()), slot::isMouseOver)
            .entries(
                options?.map { EntryStacks.of(ItemStack(it.value(), count)) }
                    ?: listOf(EntryStacks.of(slot.slot.item))
                )
            .apply {
                when (ingredientIO) {
                    IngredientIO.INPUT -> markInput()
                    IngredientIO.OUTPUT -> markOutput()
                    IngredientIO.CATALYST, IngredientIO.NONE -> {}
                }
            }
            .disableBackground()
    }

    fun ItemSlotElement<ItemSlot>.reiWidget(ingredientIO: IngredientIO = IngredientIO.NONE, options: HolderSet<out ItemLike>? = null, count: Int = 1, itemListener: (ItemLike?) -> Unit = {}): ItemSlotElement<ItemSlot> {
        this.element.addEventListener(REI_WIDGET_EVENT) { event ->
            event.customData.let { it as? ReiSlotWidgetHolder }?.also { widgetHolder ->
                val slot = wrapSlot(this.element, ingredientIO, options, count)
                itemListener(slot.currentEntry.value.let { it as? ItemStack }?.item)
                @Suppress("UnstableApiUsage")
                slot.withEntriesListener { slot ->
                    itemListener(slot.currentEntry.value.let { it as? ItemStack }?.item)
                }
                widgetHolder.list.add(slot)
            }
        }
        return this
    }

    val REI_WIDGET_EVENT = "reiWidget"

    private class InvisibleEntryWidget(point: Point, val mouseOver: (Float, Float) -> Boolean) : EntryWidget(point) {
        init {
            noBackground()
            noHighlight()
        }

        private val localToWorld = Matrix3x2f()

        override fun drawCurrentEntry(graphics: GuiGraphics?, mouseX: Int, mouseY: Int, delta: Float) = Unit

        override fun containsMouse(mouseX: Double, mouseY: Double): Boolean {
            val worldMouse = getWorldMouse(mouseX, mouseY)
            return mouseOver(worldMouse.x, worldMouse.y)
        }

        override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
            val matrixStack = graphics.pose()
            matrixStack.pushMatrix()
            matrixStack.translate(bounds.x.toFloat(), bounds.y.toFloat())
            graphics.pose().invert(localToWorld)
            super.render(graphics, mouseX, mouseY, delta)
            matrixStack.popMatrix()

            currentEntry //force to process entries
        }

        fun getWorldMouse(mouseX: Float, mouseY: Float): Vector2f {
            return localToWorld
                .transformPosition(Vector2f(0f, 0f))
                .mul(-1f).add(mouseX - bounds.x, mouseY - bounds.y)
        }

        fun getWorldMouse(mouseX: Number, mouseY: Number): Vector2f =
            getWorldMouse(mouseX.toFloat(), mouseY.toFloat())
    }

    data class ReiSlotWidgetHolder(val list: MutableList<in Widget>)
}
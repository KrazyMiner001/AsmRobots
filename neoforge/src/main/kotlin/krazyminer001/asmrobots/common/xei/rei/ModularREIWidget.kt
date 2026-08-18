package krazyminer001.asmrobots.common.xei.rei

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.ModularUIClientAccess
import com.lowdragmc.lowdraglib2.gui.ui.ModularUIWidget
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics
import me.shedaniel.rei.api.client.gui.widgets.Widget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import org.joml.Matrix3x2f
import org.joml.Vector2f

class ModularREIWidget(val modularUI: ModularUI, val rectangle: Rectangle) : Widget() {

    private var currentTick = 0f

    private val localToWorld = Matrix3x2f()
    private val modularWidget: ModularUIWidget
        get() = ModularUIClientAccess.getWidget(modularUI)

    fun getWorldMouse(mouseX: Float, mouseY: Float): Vector2f {
        return localToWorld
            .transformPosition(Vector2f(0f, 0f))
            .mul(-1f).add(mouseX - rectangle.x, mouseY - rectangle.y)
    }

    fun getWorldMouse(mouseX: Number, mouseY: Number): Vector2f =
        getWorldMouse(mouseX.toFloat(), mouseY.toFloat())

    fun getWorldMouseNormal(mouseX: Float, mouseY: Float): Vector2f {
        return localToWorld
            .transformDirection(Vector2f(0f, 0f))
            .mul(-1f).add(mouseX, mouseY)
    }

    fun getWorldMouseNormal(mouseX: Number, mouseY: Number): Vector2f =
        getWorldMouseNormal(mouseX.toFloat(), mouseY.toFloat())

    override fun children(): List<GuiEventListener> = listOf()

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        val partialTick = minecraft.deltaTracker.getGameTimeDeltaPartialTick(false)
        if (currentTick.toInt() < (currentTick + partialTick).toInt()) {
            modularUI.tick()
        }
        currentTick += partialTick
        graphics.renderDeferredElements()

        val matrixStack = graphics.pose()
        matrixStack.pushMatrix()
        matrixStack.translate(rectangle.x.toFloat(), rectangle.y.toFloat())
        graphics.pose().invert(localToWorld)
        val worldMouse = getWorldMouse(mouseX, mouseY)

        modularWidget
            .extractRenderState(
                graphics,
                worldMouse.x.toInt(),
                worldMouse.y.toInt(),
                partialTick
            )

        matrixStack.popMatrix()
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        return modularWidget.mouseClicked(
            event.toWorldMouse(),
            doubleClick
        )
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        return modularWidget.mouseReleased(
            event.toWorldMouse()
        )
    }

    override fun mouseDragged(event: MouseButtonEvent, dx: Double, dy: Double): Boolean {
        val worldDrag = getWorldMouseNormal(dx, dy)
        return modularWidget.mouseDragged(
            event.toWorldMouse(),
            worldDrag.x.toDouble(), worldDrag.y.toDouble()
        )
    }

    override fun mouseScrolled(x: Double, y: Double, scrollX: Double, scrollY: Double): Boolean {
        val worldMouse = getWorldMouse(x, y)

        return modularWidget.mouseScrolled(
            worldMouse.x.toDouble(),
            worldMouse.y.toDouble(),
            scrollX, scrollY
        )
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        return modularWidget.keyPressed(event)
    }

    override fun keyReleased(event: KeyEvent): Boolean {
        return modularWidget.keyReleased(event)
    }

    override fun setFocused(focused: Boolean) {
        modularWidget.isFocused = focused
    }

    override fun isFocused(): Boolean {
        return modularWidget.isFocused
    }

    fun MouseButtonEvent.toWorldMouse(): MouseButtonEvent {
        val worldMouse = getWorldMouse(this.x, this.y)
        return MouseButtonEvent(worldMouse.x.toDouble(), worldMouse.y.toDouble(), this.buttonInfo)
    }
}
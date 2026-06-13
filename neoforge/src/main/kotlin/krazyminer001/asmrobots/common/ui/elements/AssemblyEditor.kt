package krazyminer001.asmrobots.common.ui.elements

import com.lowdragmc.lowdraglib2.gui.ui.elements.BindableUIElement
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents
import com.lowdragmc.lowdraglib2.gui.ui.rendering.DelegatingUIElementRenderer
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.gui.ui.rendering.IGUIContext
import com.lowdragmc.lowdraglib2.gui.util.DrawerHelperClient
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegisterClient
import net.minecraft.client.Minecraft
import java.awt.Color

@LDLRegister(
    name = "assembly-editor",
    registry = "ldlib2:ui-element",
)
class AssemblyEditor(var text: String = "") : BindableUIElement<String>() {

    init {
        layout.height(14f)
        layout.paddingAll(2f)

        isFocusable = true

        addEventListener(UIEvents.CHAR_TYPED, ::charTyped)
    }

    var cursorPosX = 0
    var cursorPosY = 0

    private fun appendChar(char: Char) {
        val currentContent = text.split("\n").toMutableList()
        val line = currentContent[cursorPosY]
        currentContent[cursorPosY] = line
            .toCharArray()
            .toMutableList()
            .also { it.add(cursorPosX++, char) }
            .toCharArray()
            .concatToString()

        text = currentContent.joinToString("\n")
    }

    override fun getValue(): String {
        return text
    }

    override fun setValue(
        value: String?,
        notify: Boolean
    ): AssemblyEditor {
        text = value ?: ""
        if (notify) notifyListeners()
        return this
    }

    private fun charTyped(uiEvent: UIEvent) {
        appendChar(uiEvent.codePoint)
        notifyListeners()
    }

    @LDLRegisterClient(name = "assembly_editor", registry = "ldlib2:ui_element_renderer")
    class AssemblyEditorRenderer : DelegatingUIElementRenderer<AssemblyEditor, AssemblyEditorRenderer>() {
        override fun type(): Class<AssemblyEditor> = AssemblyEditor::class.java

        override fun drawBackgroundAdditional(element: AssemblyEditor, context: IGUIContext) {
            if (context !is GUIContext) {
                drawParentBackgroundAdditional(element, context)
                return
            }

            DrawerHelperClient.drawSolidRect(context,
                element.positionX,
                element.positionY,
                element.sizeWidth,
                element.sizeHeight,
                Color.BLACK.rgb
            )

            val font = Minecraft.getInstance().font

            element.text.split("\n").forEachIndexed { index, line ->
                val lineY = element.contentY + index * (font.lineHeight + 1)
                val lineX = element.contentX

                context.pose.pushPose()
                context.pose.translate(lineX, lineY)
                context.graphics.text(Minecraft.getInstance().font, line, 0, 0, Color.RED.rgb)
                context.pose.popPose()
            }
        }
    }
}
package krazyminer001.asmrobots.common.ui.elements

import com.lowdragmc.lowdraglib2.gui.ui.elements.TextArea
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import krazyminer001.asmrobots.common.asm.LexedLine
import krazyminer001.asmrobots.common.asm.instructions.InstructionArgument
import krazyminer001.asmrobots.common.asm.lex
import net.minecraft.client.gui.Font
import net.minecraft.locale.Language
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style
import java.awt.Color

@LDLRegister(
    name = "assembly-editor",
    registry = "ldlib2:ui-element",
)
class AssemblyEditor : TextArea() {
    private var needsReparsing: Boolean = true
    private val styledLines: MutableList<FormattedText> = mutableListOf()

    private fun reparseAndStyle() {
        styledLines.clear()
        val lexedText = lex(lines.joinToString("\n")).successValue ?: return
        lexedText.zip(lines).mapTo(styledLines) { (lexedLine, rawLine) ->
            val textComponents = mutableListOf<FormattedText>()
            if (lexedLine.content != null) {
                when (lexedLine.content) {
                    is LexedLine.Content.Label ->
                        textComponents
                            .add(FormattedText.of(
                                lexedLine.content.name + ":",
                                Style.EMPTY.withColor(Color.RED.rgb)
                            ))
                    is LexedLine.Content.Instruction -> {
                        textComponents.add(FormattedText.of(
                            lexedLine.content.instruction.name,
                            Style.EMPTY.withColor(Color.BLUE.rgb)
                        ))
                        lexedLine.content.arguments.mapTo(textComponents) {
                            when (it) {
                                is InstructionArgument.Condition -> FormattedText.of(it.condition.name, Style.EMPTY.withColor(Color.GREEN.rgb))
                                is InstructionArgument.Immediate32 -> FormattedText.of(it.value.toString(), Style.EMPTY.withColor(Color.ORANGE.rgb))
                                is InstructionArgument.ImmediateFloat32 -> FormattedText.of(it.value.toString(), Style.EMPTY.withColor(Color.YELLOW.rgb))
                                is InstructionArgument.Label -> FormattedText.of(it.name!!, Style.EMPTY.withColor(Color.RED.rgb))
                                is InstructionArgument.Pointer -> TODO()
                                is InstructionArgument.Register -> FormattedText.of(it.register.name, Style.EMPTY.withColor(Color.PINK.rgb))
                            }
                        }
                    }
                }
            }
            if (lexedLine.comment != null) {
                textComponents.add(FormattedText.of("//${lexedLine.comment}", Style.EMPTY.withColor(Color.GRAY.rgb)))
            }

            if (textComponents.isEmpty()) textComponents.add(FormattedText.of(rawLine, Style.EMPTY.withColor(Color.CYAN.rgb)))

            FormattedText.composite(textComponents)
        }
        needsReparsing = false
    }

    override fun onRawLinesUpdated() {
        super.onRawLinesUpdated()
        needsReparsing = true
    }

    override fun drawContentLines(
        context: GUIContext,
        font: Font,
        scale: Float,
        x: Float,
        y: Float,
        firstVisibleLine: Int,
        lastVisibleLine: Int
    ) {
        if (needsReparsing) reparseAndStyle()
        styledLines
            .withIndex()
            .filter { it.index in (firstVisibleLine..lastVisibleLine) }
            .forEach { (index, line) ->
                val lineY = y + index * lineHeight() - scrollY
                val drawX = x - scrollX
                val charSequence = Language.getInstance().getVisualOrder(line)

                context.pose.pushPose()
                context.pose.translate(drawX, lineY)
                context.graphics.text(
                    font,
                    charSequence,
                    0,
                    0,
                    -1,
                    textAreaStyle.textShadow()
                )
                context.pose.popPose()
            }
    }
}
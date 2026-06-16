package krazyminer001.asmrobots.common.ui.elements

import com.lowdragmc.lowdraglib2.gui.ui.elements.TextArea
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import krazyminer001.asmrobots.common.asm.Lexeme
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
        val lexedText = lex(lines.joinToString("\n"))
        lexedText.mapTo(styledLines) { line ->
            FormattedText.composite(line.map {
                FormattedText.of(
                    it.toString(),
                    when (it) {
                        Lexeme.Colon -> Style.EMPTY
                        Lexeme.Comma -> Style.EMPTY
                        is Lexeme.Comment -> Style.EMPTY.withColor(Color.gray.rgb)
                        is Lexeme.Condition -> Style.EMPTY.withColor(Color.yellow.rgb)
                        is Lexeme.Error -> Style.EMPTY.withColor(Color.red.rgb).withUnderlined(true)
                        is Lexeme.FloatNum -> Style.EMPTY.withColor(Color.cyan.rgb)
                        is Lexeme.Identifier -> Style.EMPTY.withColor(0x8314D9)
                        is Lexeme.Integer -> Style.EMPTY.withColor(Color.cyan.rgb)
                        Lexeme.LeftBracket -> Style.EMPTY
                        is Lexeme.Mnemonic -> Style.EMPTY.withColor(Color.green.rgb)
                        is Lexeme.Register -> Style.EMPTY.withColor(Color.pink.rgb)
                        Lexeme.RightBracket -> Style.EMPTY
                        Lexeme.Whitespace -> Style.EMPTY
                    }
                )
            })
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
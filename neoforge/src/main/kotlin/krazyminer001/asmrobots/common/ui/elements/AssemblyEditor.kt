package krazyminer001.asmrobots.common.ui.elements

import com.lowdragmc.lowdraglib2.gui.LDLibFonts
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextArea
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.gui.ui.style.PropertyRegistry
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import krazyminer001.asmrobots.common.asm.AsmError
import krazyminer001.asmrobots.common.asm.Assembler
import krazyminer001.asmrobots.common.asm.Lexeme
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.max

@LDLRegister(
    name = "assembly-editor",
    registry = "ldlib2:ui-element",
)
class AssemblyEditor : TextArea() {
    private var needsReparsing: Boolean = true
    private val styledLines: MutableList<Pair<Component, AsmError.ParseError?>> = mutableListOf()

    var indentSize: Int = 2

    private fun reparseAndStyle() {
        styledLines.clear()
        val lexedText = Assembler.lex(lines.joinToString("\n"))
        lexedText.mapTo(styledLines) { line ->
            val component = Component.empty()
            line.forEach { lexeme ->
                component.append(
                    Component.literal(
                        lexeme.toString()
                    ).withStyle {
                        when (lexeme) {
                            Lexeme.Colon -> it
                            Lexeme.Comma -> it
                            is Lexeme.Comment -> it.withColor(Color.gray.rgb)
                            is Lexeme.Condition -> it.withColor(Color.yellow.rgb)
                            is Lexeme.Error -> it.withColor(Color.red.rgb).withUnderlined(true)
                            is Lexeme.FloatNum -> it.withColor(Color.cyan.rgb)
                            is Lexeme.Identifier -> it.withColor(0xCB94F5)
                            is Lexeme.Integer -> it.withColor(Color.cyan.rgb)
                            Lexeme.LeftBracket -> it
                            is Lexeme.Mnemonic -> it.withColor(Color.green.rgb)
                            is Lexeme.Register -> it.withColor(Color.pink.rgb)
                            Lexeme.RightBracket -> it
                            Lexeme.Whitespace -> it
                            is Lexeme.Byte -> it.withColor(Color.cyan.rgb)
                            Lexeme.EmbedDirective -> it.withColor(Color.yellow.rgb)
                        }
                    }
                )
            }
            val parsed = Assembler.parse(line)
            Pair(component, parsed.errorValue)
        }
        needsReparsing = false
    }

    init {
        textAreaStyle.setDefault(PropertyRegistry.FONT, LDLibFonts.JETBRAINS_MONO_BOLD)
        internalSetup()
    }

    override fun insertNewLine() {
        val cursorLine = cursorLine
        super.insertNewLine()
        val lastLine = lines[cursorLine]
        insertText(lastLine.takeWhile { it.isWhitespace() })
    }

    override fun onKeyDown(event: UIEvent) {
        if (isEditable) {
            when (event.keyCode) {
                GLFW.GLFW_KEY_TAB -> insertText(" ".repeat(indentSize))
                GLFW.GLFW_KEY_SLASH -> {
                    if (isCtrlOrCmdDown()) {
                        pushHistory()
                        if (!hasSelection()) {
                            val delta: Int
                            val lineNum = cursorLine
                            val line = lines[lineNum]

                            if (line.startsWith("//")) {
                                lines[lineNum] = line.drop(2)
                                delta = -2
                            } else {
                                lines[lineNum] = "//$line"
                                delta = 2
                            }
                            setCursor(cursorLine, max(0, cursorCol + delta))
                        } else {
                            var start = selStartLine
                            var end = selEndLine
                            if (start > end) {
                                start = end.also { end = start }
                            }
                            val selectedLines = lines
                                .withIndex()
                                .filter { (index, _) -> index in start..end }
                                .toMutableList()
                            val delta: Int
                            if (selectedLines.all { (_, line) -> line.startsWith("//") }) {
                                selectedLines.forEach { (index, line) ->
                                    lines[index] = line.drop(2)
                                }
                                delta = -2
                            } else {
                                selectedLines.forEach { (index, line) ->
                                    lines[index] = "//$line"
                                }
                                delta = 2
                            }

                            if (cursorLine in start..end) {
                                setCursor(cursorLine, max(0, cursorCol + delta))
                            }
                        }
                        onRawLinesUpdated()
                    }
                }
                else -> super.onKeyDown(event)
            }
        } else {
            super.onKeyDown(event)
        }
    }

    override fun setValue(value: Array<out String?>?): TextArea {
        super.setValue(value)
        needsReparsing = true
        return this
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
                val (text, error) = line
                val lineY = y + index * lineHeight() - scrollY
                val drawX = x - scrollX
                context.pose.pushPose()
                context.pose.translate(drawX, lineY)
                context.graphics.text(
                    font,
                    text.copy().withStyle { style ->
                        style.withFont(FontDescription.Resource(textAreaStyle.font()))
                    }.let { component ->
                        if (error != null) {
                            component.append(Component.literal("  Error: ${error.text}").withStyle {it.withColor(Color.red.rgb)})
                        } else {
                            component
                        }
                    },
                    0,
                    0,
                    -1,
                    textAreaStyle.textShadow()
                )
                context.pose.popPose()
            }
    }
}
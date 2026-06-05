package krazyminer001.asmrobots.common.asm

import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.LanguageDefinition
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.StyleManager
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.TokenType
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.language.TokenTypes
import net.minecraft.network.chat.Style
import java.awt.Color

object AsmLanguageDefinition
    : LanguageDefinition(
        "Asm",
        listOf(
            TokenType("Label").setPattern(".*:"),
            TokenType("Instruction").setPattern("\\b(?:" +
                Instruction::class.sealedSubclasses.joinToString("|") {
                    it.simpleName!!.lowercase()
                } + ")\\b"
            ),
            TokenType("Register").setPattern("\\b(?:" +
                Register.entries.joinToString("|") {
                    it.name.lowercase()
                } + ")\\b"
            ),
            TokenType("Literal").setPattern("-?\\d+"),
            TokenType("Comment").setPattern("//.*$"),
            TokenTypes.WHITESPACE,
            TokenTypes.OTHER
        ),
        setOf(":")
    )

object AsmStyleManager : StyleManager() {
    init {
        styleMap["Label"] = Style.EMPTY.withColor(Color.red.rgb)
        styleMap["Instruction"] = Style.EMPTY.withColor(Color.cyan.rgb)
        styleMap["Register"] = Style.EMPTY.withColor(Color.yellow.rgb)
        styleMap["Literal"] = Style.EMPTY.withColor(Color.yellow.rgb)
        styleMap["Comment"] = Style.EMPTY.withColor(Color.gray.rgb)
    }
}
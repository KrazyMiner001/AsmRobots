package krazyminer001.asmrobots.common.ui.elements

import com.lowdragmc.lowdraglib2.gui.ui.UIContainer
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextAreaElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextAreaSpec

fun assemblyEditor(
    spec: (TextAreaSpec<AssemblyEditor>.() -> Unit)? = null,
    init: TextAreaElement<AssemblyEditor>.() -> Unit = {}
): AssemblyEditor {
    return TextAreaElement(AssemblyEditor(), spec).apply(init).build()
}

fun UIContainer<*, *>.assemblyEditor(
    spec: (TextAreaSpec<AssemblyEditor>.() -> Unit)? = null,
    init: TextAreaElement<AssemblyEditor>.() -> Unit = {}
): TextAreaElement<AssemblyEditor> {
    return add(TextAreaElement(AssemblyEditor(), spec), init)
}
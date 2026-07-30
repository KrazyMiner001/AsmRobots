package krazyminer001.asmrobots.common.item.upgrade

import krazyminer001.asmrobots.common.asm.extension.Extension

class ExtensionUpgradeItem(
    properties: Properties,
    override vararg val extensions: Extension
) : UpgradeItem(properties) {
    companion object {
        fun withExtensions(vararg extensions: Extension) = { properties: Properties ->
            ExtensionUpgradeItem(properties, *extensions)
        }
    }
}
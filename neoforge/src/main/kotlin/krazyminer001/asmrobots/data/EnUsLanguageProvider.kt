package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.item.ModCreativeTabs
import krazyminer001.asmrobots.common.item.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.contents.TranslatableContents
import net.neoforged.neoforge.common.data.LanguageProvider

class EnUsLanguageProvider(output: PackOutput) : LanguageProvider(output, AsmRobots.ID, "en_us") {

    override fun addTranslations() {
        this.add(ModEntities.ROBOT_ENTITY, "Robot")

        this.add(ModItems.GPS_MODULE, "GPS Module")
        this.add(ModItems.CHEST_MODULE, "Chest Module")
        this.add(ModItems.HARD_DRIVE_MODULE, "Hard Drive Module")
        this.add(ModItems.STORAGE_CONTROLLER_MODULE, "Storage Controller Module")
        this.add(ModItems.CRAFTING_TABLE_MODULE, "Crafting Table Module")
        this.add(ModItems.NETWORKING_MODULE, "Networking Module")
        this.add(ModItems.STORAGE_BLOCK_INTERFACE_MODULE, "Storage Block Interface Module")

        this.add(ModItems.SPEED_UPGRADE, "Speed Upgrade")
        this.add(ModItems.PROCESSING_SPEED_UPGRADE, "Processing Speed Upgrade")
        this.add(ModItems.STEP_HEIGHT_UPGRADE, "Step Height Upgrade")

        this.add(ModItems.ROBOT, "Robot")

        this.add((ModCreativeTabs.MAIN_TAB.displayName.contents as TranslatableContents).key, "ASM Robots")

        this.add(ModBlocks.RELAY_BLOCK, "Relay Block")
    }
}
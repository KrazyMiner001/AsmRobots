package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.item.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class EnUsLanguageProvider(output: PackOutput) : LanguageProvider(output, AsmRobots.ID, "en_us") {

    override fun addTranslations() {
        this.add(ModEntities.ROBOT_ENTITY, "Robot")

        this.add(ModItems.GPS_MODULE, "GPS Module")
        this.add(ModItems.CHEST_MODULE, "Chest Module")
        this.add(ModItems.HARD_DRIVE_MODULE, "Hard Drive Module")
        this.add(ModItems.STORAGE_CONTROLLER_MODULE, "Storage Controller Module")
        this.add(ModItems.CRAFTING_TABLE_MODULE, "Crafting Table Module")
    }
}
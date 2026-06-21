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
    }
}
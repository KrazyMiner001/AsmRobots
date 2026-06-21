package krazyminer001.asmrobots.common.item.component

import com.mojang.serialization.Codec
import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModComponents {
    val REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AsmRobots.ID)

    val HARD_DRIVE: DataComponentType<List<Byte>> by REGISTRAR.registerComponentType("hard_drive") { builder ->
        builder.persistent(Codec.BYTE.listOf(256, 256))
    }
}
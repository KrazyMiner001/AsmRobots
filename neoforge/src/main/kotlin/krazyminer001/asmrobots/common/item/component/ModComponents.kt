package krazyminer001.asmrobots.common.item.component

import com.mojang.serialization.Codec
import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModComponents {
    val REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AsmRobots.ID)

    val HARD_DRIVE: DataComponentType<List<Byte>> by REGISTRY.registerComponentType("hard_drive") { builder ->
        builder.persistent(Codec.BYTE.listOf(256, 256))
    }

    val SOLID_STATE_DRIVE: DataComponentType<List<Byte>> by REGISTRY.registerComponentType("solid_state_drive") { builder ->
        builder.persistent(Codec.BYTE.listOf(128, 128))
    }

    val STORAGE_COMPONENT: DataComponentType<StorageModuleComponent> by REGISTRY.registerComponentType("storage_component") { builder ->
        builder.persistent(StorageModuleComponent.CODEC)
            .networkSynchronized(StorageModuleComponent.STREAM_CODEC)
    }
}
package krazyminer001.asmrobots.common.block

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModBlockEntities {
    val REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AsmRobots.ID)

    val RELAY_BLOCK_ENTITY_TYPE by REGISTRY.register("relay_block_entity") {->
        BlockEntityType(
            ::RelayBlockEntity,
            false,
            ModBlocks.RELAY_BLOCK
        )
    }
}
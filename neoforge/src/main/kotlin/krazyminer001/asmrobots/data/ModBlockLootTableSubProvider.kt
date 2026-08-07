package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.block.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class ModBlockLootTableSubProvider(registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, registries) {
    override fun generate() {
        this.dropSelf(ModBlocks.RELAY_BLOCK)
    }

    override fun getKnownBlocks(): Iterable<Block> {
        return ModBlocks.REGISTRY.entries
            .stream()
            .map { it.value() }
            .toList()
    }
}
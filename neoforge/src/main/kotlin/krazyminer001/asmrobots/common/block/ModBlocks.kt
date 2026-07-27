package krazyminer001.asmrobots.common.block

import krazyminer001.asmrobots.common.AsmRobots
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModBlocks {
    val REGISTRY = DeferredRegister.createBlocks(AsmRobots.ID)

    val RELAY_BLOCK by REGISTRY.registerBlock("relay_block", ::RelayBlock)
}
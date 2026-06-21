package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.AsmRobots
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY = DeferredRegister.createItems(AsmRobots.ID)

    val GPS_MODULE by REGISTRY.registerItem("gps_module", ::GPSModule) { properties ->
        properties
    }
}
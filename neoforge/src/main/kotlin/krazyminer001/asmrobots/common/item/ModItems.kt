package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.item.component.ModComponents
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY = DeferredRegister.createItems(AsmRobots.ID)

    val GPS_MODULE by REGISTRY.registerItem("gps_module", ::GPSModule) { properties ->
        properties
    }

    val HARD_DRIVE_MODULE by REGISTRY.registerItem("hard_drive_module", ::HardDriveModule) { properties ->
        properties.component(ModComponents.HARD_DRIVE, List(256) { 0 })
    }
}
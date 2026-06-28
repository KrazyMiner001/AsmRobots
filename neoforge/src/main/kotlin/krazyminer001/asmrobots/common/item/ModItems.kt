package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.component.StorageModuleComponent
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY = DeferredRegister.createItems(AsmRobots.ID)

    val GPS_MODULE by REGISTRY.registerItem("gps_module", ::GPSModule) { properties ->
        properties
            .stacksTo(1)
    }

    val HARD_DRIVE_MODULE by REGISTRY.registerItem("hard_drive_module", ::HardDriveModule) { properties ->
        properties
            .component(ModComponents.HARD_DRIVE, List(256) { 0 })
            .stacksTo(1)
    }

    val STORAGE_CONTROLLER_MODULE by REGISTRY.registerItem("storage_controller_module", ::StorageControllerModule) {properties ->
        properties
            .stacksTo(1)
    }

    val CHEST_MODULE by REGISTRY.registerItem("chest_module", ::ChestModule) { properties ->
        properties
            .component(ModComponents.STORAGE_COMPONENT, StorageModuleComponent(size = 8))
            .stacksTo(1)
    }
}
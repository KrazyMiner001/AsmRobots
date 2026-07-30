package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.asm.extension.InstructionExtension
import krazyminer001.asmrobots.common.attribute.ModAttributes
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.component.StorageModuleComponent
import krazyminer001.asmrobots.common.item.module.*
import krazyminer001.asmrobots.common.item.upgrade.AttributeUpgradeItem
import krazyminer001.asmrobots.common.item.upgrade.ExtensionUpgradeItem
import krazyminer001.asmrobots.common.item.upgrade.ProcessingSpeedUpgrade
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.SpawnEggItem
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

    val STORAGE_CONTROLLER_MODULE by REGISTRY.registerItem(
        "storage_controller_module",
        ::StorageControllerModule
    ) { properties ->
        properties
            .stacksTo(1)
    }

    val CHEST_MODULE by REGISTRY.registerItem("chest_module", ::ChestModule) { properties ->
        properties
            .component(ModComponents.STORAGE_COMPONENT, StorageModuleComponent(size = 8))
            .stacksTo(1)
    }

    val CRAFTING_TABLE_MODULE by REGISTRY.registerItem("crafting_table_module", ::CraftingTableModule) { properties ->
        properties
            .component(ModComponents.STORAGE_COMPONENT, StorageModuleComponent(size = 10))
            .stacksTo(1)
    }

    val ROBOT by REGISTRY.registerItem("robot", ::SpawnEggItem) { properties ->
        properties
            .spawnEgg(ModEntities.ROBOT_ENTITY)
            .stacksTo(1)
    }

    val NETWORKING_MODULE by REGISTRY.registerItem("networking_module", ::NetworkingModule) { properties ->
        properties.stacksTo(1)
    }

    val STORAGE_BLOCK_INTERFACE_MODULE by REGISTRY.registerItem(
        "storage_block_interface_module",
        ::StorageBlockInterfaceModule
    ) { properties ->
        properties.stacksTo(1)
            .component(ModComponents.STORAGE_COMPONENT, StorageModuleComponent(size = 1))
    }

    val RELAY_BLOCK by REGISTRY.registerSimpleBlockItem("relay_block") { ModBlocks.RELAY_BLOCK }

    val SPEED_UPGRADE by REGISTRY.registerItem(
        "speed_upgrade",
        AttributeUpgradeItem.withModifiers(
            Pair(Attributes.MOVEMENT_SPEED, ModAttributes.SPEED_UPGRADE_ATTRIBUTE_MODIFIER)
        )
    ) { properties -> properties.stacksTo(1) }

    val PROCESSING_SPEED_UPGRADE by REGISTRY.registerItem(
        "processing_speed_upgrade",
        ::ProcessingSpeedUpgrade,
    ) { properties ->
        properties.stacksTo(1)
    }

    val STEP_HEIGHT_UPGRADE by REGISTRY.registerItem(
        "step_height_upgrade",
        AttributeUpgradeItem.withModifiers(
            Pair(Attributes.MOVEMENT_SPEED, ModAttributes.STEP_HEIGHT_UPGRADE_ATTRIBUTE_MODIFIER)
        )
    ) { properties -> properties.stacksTo(1) }

    val FLOATING_POINT_UPGRADE by REGISTRY.registerItem(
        "floating_point_upgrade",
        ExtensionUpgradeItem.withExtensions(
            InstructionExtension.FLOATING_POINT_ARITHMETIC
        )
    ) { properties -> properties.stacksTo(1) }
}
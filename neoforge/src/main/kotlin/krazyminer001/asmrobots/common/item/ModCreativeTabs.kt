package krazyminer001.asmrobots.common.item

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.block.ModBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModCreativeTabs {
    val REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AsmRobots.ID)

    val MAIN_TAB by REGISTRY.register("main") { ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.${AsmRobots.ID}.main"))
            .icon { ItemStack(ModItems.ROBOT) }
            .displayItems { parameters, output ->
                output.accept(ModItems.ROBOT)

                output.accept(ModItems.GPS_MODULE)
                output.accept(ModItems.CHEST_MODULE)
                output.accept(ModItems.HARD_DRIVE_MODULE)
                output.accept(ModItems.STORAGE_CONTROLLER_MODULE)
                output.accept(ModItems.CRAFTING_TABLE_MODULE)
                output.accept(ModItems.NETWORKING_MODULE)
                output.accept(ModItems.STORAGE_BLOCK_INTERFACE_MODULE)
                output.accept(ModItems.SOLID_STATE_DRIVE_MODULE)

                output.accept(ModItems.CHUNK_LOAD_UPGRADE)
                output.accept(ModItems.MEMORY_MAP_UPGRADE)
                output.accept(ModItems.FLOATING_POINT_UPGRADE)
                output.accept(ModItems.STEP_HEIGHT_UPGRADE)
                output.accept(ModItems.PROCESSING_SPEED_UPGRADE)
                output.accept(ModItems.SPEED_UPGRADE)

                output.accept(ModBlocks.RELAY_BLOCK)
            }
            .build()
    }
}
package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.item.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.data.PackOutput

class ModModelProvider(output: PackOutput) : ModelProvider(output, AsmRobots.ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        itemModels.generateFlatItem(ModItems.GPS_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.CHEST_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.STORAGE_CONTROLLER_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.HARD_DRIVE_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.CRAFTING_TABLE_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.NETWORKING_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.STORAGE_BLOCK_INTERFACE_MODULE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.SOLID_STATE_DRIVE_MODULE, ModelTemplates.FLAT_ITEM)

        itemModels.generateFlatItem(ModItems.SPEED_UPGRADE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.PROCESSING_SPEED_UPGRADE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.STEP_HEIGHT_UPGRADE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.FLOATING_POINT_UPGRADE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.MEMORY_MAP_UPGRADE, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(ModItems.CHUNK_LOAD_UPGRADE, ModelTemplates.FLAT_ITEM)

        itemModels.generateFlatItem(ModItems.ROBOT, ModelTemplates.FLAT_ITEM)

        blockModels.createTrivialCube(ModBlocks.RELAY_BLOCK)
    }
}
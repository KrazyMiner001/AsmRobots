package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
    override fun buildRecipes() {
        shaped(RecipeCategory.MISC, ModItems.ROBOT)
            .pattern(" g ")
            .pattern("iii")
            .pattern("b b")
            .define('g', Items.GREEN_WOOL)
            .define('i', Tags.Items.INGOTS_IRON)
            .define('b', Items.BLACK_WOOL)
            .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.CHEST_MODULE)
            .pattern("pip")
            .pattern("iCi")
            .pattern("pip")
            .define('p', ItemTags.PLANKS)
            .define('C', Tags.Items.CHESTS)
            .define('i', Tags.Items.INGOTS_IRON)
            .group("chest_module")
            .unlockedBy(getHasName(Items.CHEST), has(Tags.Items.CHESTS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.GPS_MODULE)
            .pattern("rir")
            .pattern("ici")
            .pattern("rir")
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('c', Items.COMPASS)
            .define('i', Tags.Items.INGOTS_IRON)
            .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.HARD_DRIVE_MODULE)
            .pattern("CiC")
            .pattern("igi")
            .pattern("CiC")
            .define('g', Tags.Items.INGOTS_GOLD)
            .define('C', Tags.Items.CHESTS)
            .define('i', Tags.Items.INGOTS_IRON)
            .group("hard_drive_module")
            .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.CRAFTING_TABLE_MODULE)
            .pattern("rir")
            .pattern("ici")
            .pattern("rir")
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('c', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
            .define('i', Tags.Items.INGOTS_IRON)
            .group("crafting_table_module")
            .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.STORAGE_CONTROLLER_MODULE)
            .pattern("CdC")
            .pattern("drd")
            .pattern("CdC")
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.CHESTS)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .group("storage_controller_module")
            .unlockedBy(getHasName(Items.CHEST), has(Tags.Items.CHESTS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.SOLID_STATE_DRIVE_MODULE)
            .pattern("sds")
            .pattern("dHd")
            .pattern("sds")
            .define('s', Items.SUGAR)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .define('H', ModItems.HARD_DRIVE_MODULE)
            .group("solid_state_drive_module")
            .unlockedBy(getHasName(ModItems.HARD_DRIVE_MODULE), has(ModItems.HARD_DRIVE_MODULE))
            .save(output)

        RobotCraftRecipeBuilder(ItemStackTemplate(ModItems.SOLID_STATE_DRIVE_MODULE, 1), RecipeCategory.TOOLS)
            .item(Items.SUGAR, 2)
            .item(tag(Tags.Items.GEMS_DIAMOND), 1)
            .item(ModItems.HARD_DRIVE_MODULE)
            .group("solid_state_drive_module")
            .unlockedBy(getHasName(ModItems.HARD_DRIVE_MODULE), has(ModItems.HARD_DRIVE_MODULE))
            .save(output, "${AsmRobots.ID}:ssd_module_cheap")

        shaped(RecipeCategory.TOOLS, ModItems.STORAGE_BLOCK_INTERFACE_MODULE)
            .pattern("CdC")
            .pattern("dMd")
            .pattern("CdC")
            .define('C', Tags.Items.CHESTS)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .define('M', ModItems.CHEST_MODULE)
            .group("storage_block_interface_module")
            .unlockedBy(getHasName(ModItems.CHEST_MODULE), has(ModItems.CHEST_MODULE))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.NETWORKING_MODULE)
            .pattern("rdr")
            .pattern("dEd")
            .pattern("rdr")
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .define('E', Tags.Items.ENDER_PEARLS)
            .group("networking_module")
            .unlockedBy(getHasName(Items.ENDER_PEARL), has(Tags.Items.ENDER_PEARLS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.CHUNK_LOAD_UPGRADE)
            .pattern("dEd")
            .pattern("EnE")
            .pattern("dEd")
            .define('d', Tags.Items.GEMS_DIAMOND)
            .define('E', Items.ENDER_EYE)
            .define('n', Tags.Items.INGOTS_NETHERITE)
            .group("chunk_load_upgrade")
            .unlockedBy(getHasName(Items.ENDER_EYE), has(Items.ENDER_EYE))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.MEMORY_MAP_UPGRADE)
            .pattern("CrC")
            .pattern("rnr")
            .pattern("CrC")
            .define('C', Tags.Items.CHESTS)
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('n', Tags.Items.INGOTS_NETHERITE)
            .group("memory_map_upgrade")
            .unlockedBy(getHasName(ModItems.SOLID_STATE_DRIVE_MODULE), has(ModItems.SOLID_STATE_DRIVE_MODULE))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.FLOATING_POINT_UPGRADE)
            .pattern("grg")
            .pattern("rdr")
            .pattern("grg")
            .define('g', Tags.Items.INGOTS_GOLD)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .group("floating_point_upgrade")
            .unlockedBy(getHasName(Items.DIAMOND), has(Tags.Items.GEMS_DIAMOND))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.STEP_HEIGHT_UPGRADE)
            .pattern("sis")
            .pattern("iGi")
            .pattern("sis")
            .define('s', Items.OAK_STAIRS)
            .define('i', Tags.Items.INGOTS_IRON)
            .define('G', Items.GOLDEN_CARROT)
            .group("step_height_upgrade")
            .unlockedBy(getHasName(Items.GOLDEN_CARROT), has(Items.GOLDEN_CARROT))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.PROCESSING_SPEED_UPGRADE)
            .pattern("sds")
            .pattern("dSd")
            .pattern("sds")
            .define('s', Items.SUGAR)
            .define('S', ModItems.SPEED_UPGRADE)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .group("processing_speed_upgrade")
            .unlockedBy(getHasName(ModItems.SPEED_UPGRADE), has(ModItems.SPEED_UPGRADE))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.SPEED_UPGRADE)
            .pattern("sis")
            .pattern("igi")
            .pattern("sis")
            .define('s', Items.SUGAR)
            .define('i', Tags.Items.INGOTS_IRON)
            .define('g', Tags.Items.INGOTS_GOLD)
            .group("speed_upgrade")
            .unlockedBy(getHasName(Items.SUGAR), has(Items.SUGAR))
            .save(output)

        shaped(RecipeCategory.MISC, ModBlocks.RELAY_BLOCK)
            .pattern("SpS")
            .pattern("pCp")
            .pattern("SpS")
            .define('p', Tags.Items.ENDER_PEARLS)
            .define('S', Tags.Items.END_STONES)
            .define('C', Blocks.CHORUS_FLOWER)
            .group("relay_block")
            .unlockedBy(getHasName(Blocks.CHORUS_FLOWER), has(Blocks.CHORUS_FLOWER))
            .save(output)

        RobotCraftRecipeBuilder(ItemStackTemplate(ModItems.ADVANCED_NETWORKING_MODULE, 1), RecipeCategory.TOOLS)
            .item(ModItems.NETWORKING_MODULE)
            .item(tag(Tags.Items.GEMS_DIAMOND), 4)
            .item(tag(Tags.Items.DUSTS_REDSTONE), 4)
            .item(tag(Tags.Items.INGOTS_GOLD), 4)
            .item(Items.ENDER_EYE, 4)
            .group("advanced_networking_module")
            .unlockedBy(getHasName(ModItems.NETWORKING_MODULE), has(ModItems.NETWORKING_MODULE))
            .save(output)
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
        RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(
            registries: HolderLookup.Provider,
            output: RecipeOutput
        ) = ModRecipeProvider(registries, output)

        override fun getName() = "Asm Robots Recipe Provider"
    }
}
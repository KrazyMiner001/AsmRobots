package krazyminer001.asmrobots.data

import krazyminer001.asmrobots.common.item.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
    override fun buildRecipes() {
        shaped(RecipeCategory.MISC, ModItems.ROBOT)
            .pattern(" g ")
            .pattern("iii")
            .pattern("b b")
            .define('g', Items.GREEN_WOOL)
            .define('i', Items.IRON_INGOT)
            .define('b', Items.BLACK_WOOL)
            .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.CHEST_MODULE)
            .pattern("pip")
            .pattern("iCi")
            .pattern("pip")
            .define('p', ItemTags.PLANKS)
            .define('C', Tags.Items.CHESTS)
            .define('i', Items.IRON_INGOT)
            .group("chest_module")
            .unlockedBy(getHasName(Items.CHEST), has(Tags.Items.CHESTS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.GPS_MODULE)
            .pattern("rir")
            .pattern("ici")
            .pattern("rir")
            .define('r', Items.REDSTONE)
            .define('c', Items.COMPASS)
            .define('i', Items.IRON_INGOT)
            .unlockedBy(getHasName(Items.COMPASS), has(Items.COMPASS))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.HARD_DRIVE_MODULE)
            .pattern("CiC")
            .pattern("igi")
            .pattern("CiC")
            .define('g', Items.GOLD_INGOT)
            .define('C', Tags.Items.CHESTS)
            .define('i', Items.IRON_INGOT)
            .group("hard_drive_module")
            .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.CRAFTING_TABLE_MODULE)
            .pattern("rir")
            .pattern("ici")
            .pattern("rir")
            .define('r', Items.REDSTONE)
            .define('c', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
            .define('i', Items.IRON_INGOT)
            .group("crafting_table_module")
            .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES))
            .save(output)

        shaped(RecipeCategory.TOOLS, ModItems.STORAGE_CONTROLLER_MODULE)
            .pattern("CdC")
            .pattern("drd")
            .pattern("CdC")
            .define('r', Items.REDSTONE)
            .define('C', Tags.Items.CHESTS)
            .define('d', Items.DIAMOND)
            .group("storage_controller_module")
            .unlockedBy(getHasName(Items.CHEST), has(Tags.Items.CHESTS))
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
package krazyminer001.asmrobots.client

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen
import krazyminer001.asmrobots.client.entity.RobotEntityModel
import krazyminer001.asmrobots.client.entity.RobotEntityRenderer
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.recipe.ModRecipeTypes
import krazyminer001.asmrobots.common.recipe.RobotCraftRecipe
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import krazyminer001.asmrobots.data.EnUsLanguageProvider
import krazyminer001.asmrobots.data.ModBlockLootTableSubProvider
import krazyminer001.asmrobots.data.ModModelProvider
import krazyminer001.asmrobots.data.ModRecipeProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RecipesReceivedEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(AsmRobots.ID, dist = [Dist.CLIENT])
object AsmRobotsClient {
    private val _robotRecipes: MutableList<RecipeHolder<RobotCraftRecipe>> = mutableListOf()

    val ROBOT_RECIPES: List<RecipeHolder<RobotCraftRecipe>>
        get() = ArrayList(_robotRecipes)

    init {
        MOD_BUS.addListener(::registerEntityRenderers)
        MOD_BUS.addListener(::registerMenuScreens)
        MOD_BUS.addListener(::registerLayerDefinitions)
        MOD_BUS.addListener(::gatherData)

        NeoForge.EVENT_BUS.addListener(::recipesReceived)
        NeoForge.EVENT_BUS.addListener(::clientLoggingOut)
    }
    
    fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntities.ROBOT_ENTITY, ::RobotEntityRenderer)
    }
    
    fun registerMenuScreens(event: RegisterMenuScreensEvent) {
        event.register(ModMenuTypes.ROBOT_UI, ::ModularUIContainerScreen)
    }

    fun registerLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(RobotEntityModel.LAYER_LOCATION, RobotEntityModel::createBodyLayer)
    }

    fun gatherData(event: GatherDataEvent.Client) {
        event.createProvider(::ModModelProvider)
        event.createProvider(::EnUsLanguageProvider)
        event.createProvider(ModRecipeProvider::Runner)
        event.createProvider { output, lookupProvider ->
            LootTableProvider(
                output, setOf(), listOf(
                    LootTableProvider.SubProviderEntry(
                        ::ModBlockLootTableSubProvider,
                        LootContextParamSets.BLOCK
                    )
                ), lookupProvider
            )
        }
    }

    fun recipesReceived(event: RecipesReceivedEvent) {
        _robotRecipes.clear()
        _robotRecipes.addAll(event.recipeMap.byType(ModRecipeTypes.ROBOT_CRAFT))
    }

    fun clientLoggingOut(event: ClientPlayerNetworkEvent.LoggingOut) {
        _robotRecipes.clear()
    }
}
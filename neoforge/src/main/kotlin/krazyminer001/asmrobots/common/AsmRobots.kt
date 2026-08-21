package krazyminer001.asmrobots.common

import guideme.Guide
import krazyminer001.asmrobots.common.block.ModBlockEntities
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.entity.ModEntityDataSerializers
import krazyminer001.asmrobots.common.item.ModCreativeTabs
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.recipe.ModRecipeBookCategories
import krazyminer001.asmrobots.common.recipe.ModRecipeSerializers
import krazyminer001.asmrobots.common.recipe.ModRecipeTypes
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import krazyminer001.asmrobots.common.world.ModTicketTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.OnDatapackSyncEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Main mod class.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(AsmRobots.ID)
object AsmRobots {
    const val ID = "asmrobots"

    val LOGGER: Logger = LogManager.getLogger(ID)

    val GUIDE: Guide

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        ModEntities.REGISTRY.register(MOD_BUS)
        ModMenuTypes.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)
        ModComponents.REGISTRY.register(MOD_BUS)
        ModCreativeTabs.REGISTRY.register(MOD_BUS)
        ModBlocks.REGISTRY.register(MOD_BUS)
        ModBlockEntities.REGISTRY.register(MOD_BUS)
        ModTicketTypes.REGISTRY.register(MOD_BUS)
        ModRecipeTypes.REGISTRY.register(MOD_BUS)
        ModRecipeSerializers.REGISTRY.register(MOD_BUS)
        ModRecipeBookCategories.REGISTRY.register(MOD_BUS)
        ModEntityDataSerializers.REGISTRY.register(MOD_BUS)

        GUIDE = Guide.builder(Identifier.fromNamespaceAndPath(ID, "guide")).build()

        NeoForge.EVENT_BUS.addListener(::onDatapackSyncEvent)

        MOD_BUS.addListener(::createDefaultAttributes)
    }

    fun createDefaultAttributes(event: EntityAttributeCreationEvent) {
        event.put(
            ModEntities.ROBOT_ENTITY,
            LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.ATTACK_DAMAGE)
                .build()
        )
    }

    fun onDatapackSyncEvent(event: OnDatapackSyncEvent) {
        event.sendRecipes(ModRecipeTypes.ROBOT_CRAFT)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun namespacedIdentifier(path: String) = Identifier.fromNamespaceAndPath(ID, path)
}

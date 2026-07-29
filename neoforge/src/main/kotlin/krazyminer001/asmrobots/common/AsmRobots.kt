package krazyminer001.asmrobots.common

import guideme.Guide
import krazyminer001.asmrobots.common.block.ModBlockEntities
import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.item.ModCreativeTabs
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
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
@EventBusSubscriber
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

        GUIDE = Guide.builder(Identifier.fromNamespaceAndPath(ID, "guide")).build()
    }

    @SubscribeEvent
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

    fun namespacedIdentifier(path: String) = Identifier.fromNamespaceAndPath(ID, path)
}

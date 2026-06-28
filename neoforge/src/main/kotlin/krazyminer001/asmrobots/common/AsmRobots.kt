package krazyminer001.asmrobots.common

import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.item.ModItems
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
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

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        ModEntities.REGISTRY.register(MOD_BUS)
        ModMenuTypes.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)
        ModComponents.REGISTRY.register(MOD_BUS)
    }

    @SubscribeEvent
    fun createDefaultAttributes(event: EntityAttributeCreationEvent) {
        event.put(
            ModEntities.ROBOT_ENTITY,
            LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.ATTACK_DAMAGE)
                .build()
        )
    }

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.Item.ITEM,
            { stack, _ -> VanillaContainerWrapper.of(StorageModuleContainer(stack)) },
            ModItems.CHEST_MODULE
        )
    }
}

package krazyminer001.asmrobots.common

import krazyminer001.asmrobots.common.block.ModBlocks
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.ui.ModMenuTypes
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

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        ModBlocks.REGISTRY.register(MOD_BUS)
        ModEntities.REGISTRY.register(MOD_BUS)
        ModMenuTypes.REGISTER.register(MOD_BUS)
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
}

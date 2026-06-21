package krazyminer001.asmrobots.client

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen
import krazyminer001.asmrobots.client.entity.RobotEntityModel
import krazyminer001.asmrobots.client.entity.RobotEntityRenderer
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.ModEntities
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import krazyminer001.asmrobots.data.EnUsLanguageProvider
import krazyminer001.asmrobots.data.ModModelProvider
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(AsmRobots.ID, dist = [Dist.CLIENT])
@EventBusSubscriber(Dist.CLIENT)
object AsmRobotsClient {
    @SubscribeEvent
    fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntities.ROBOT_ENTITY, ::RobotEntityRenderer)
    }

    @SubscribeEvent
    fun registerMenuScreens(event: RegisterMenuScreensEvent) {
        event.register(ModMenuTypes.ROBOT_UI, ::ModularUIContainerScreen)
    }

    @SubscribeEvent
    fun registerLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(RobotEntityModel.LAYER_LOCATION, RobotEntityModel::createBodyLayer)
    }

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        event.createProvider(::ModModelProvider)
        event.createProvider(::EnUsLanguageProvider)
    }
}
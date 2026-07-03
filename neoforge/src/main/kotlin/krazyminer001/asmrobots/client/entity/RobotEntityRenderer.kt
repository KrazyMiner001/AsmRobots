package krazyminer001.asmrobots.client.entity

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.resources.Identifier

class RobotEntityRenderer(context: EntityRendererProvider.Context) :
    LivingEntityRenderer<RobotEntity, ArmedEntityRenderState, RobotEntityModel>(
        context,
        RobotEntityModel(context.bakeLayer(RobotEntityModel.LAYER_LOCATION)),
        0.5f
    ) {

    override fun shouldShowName(entity: RobotEntity, distanceToCameraSq: Double): Boolean {
        return super.shouldShowName(entity, distanceToCameraSq) && entity.shouldShowName()
    }

    init {
        addLayer(RobotEntityLayer(this, context.modelSet))
    }

    override fun extractRenderState(entity: RobotEntity, state: ArmedEntityRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver, partialTicks)
    }

    override fun createRenderState(): ArmedEntityRenderState {
        return ArmedEntityRenderState()
    }

    override fun getTextureLocation(state: ArmedEntityRenderState): Identifier {
        return Identifier.fromNamespaceAndPath(AsmRobots.ID, "textures/entity/robot_entity.png")
    }
}
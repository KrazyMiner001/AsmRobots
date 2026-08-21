package krazyminer001.asmrobots.client.entity

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.resources.Identifier

class RobotEntityRenderer(context: EntityRendererProvider.Context) :
    LivingEntityRenderer<RobotEntity, RobotEntityRenderState, RobotEntityModel>(
        context,
        RobotEntityModel(context.bakeLayer(RobotEntityModel.LAYER_LOCATION)),
        0.5f
    ) {

    override fun shouldShowName(entity: RobotEntity, distanceToCameraSq: Double): Boolean {
        return super.shouldShowName(entity, distanceToCameraSq) && entity.shouldShowName()
    }

    init {
        addLayer(RobotEntityLayer(this, context.modelSet))
        addLayer(RobotEntityFaceRenderLayer(this))
    }

    override fun extractRenderState(entity: RobotEntity, state: RobotEntityRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, itemModelResolver, partialTicks)
        state.face = entity.face
    }

    override fun createRenderState(): RobotEntityRenderState {
        return RobotEntityRenderState()
    }

    override fun getTextureLocation(state: RobotEntityRenderState): Identifier {
        return Identifier.fromNamespaceAndPath(AsmRobots.ID, "textures/entity/robot_entity.png")
    }
}
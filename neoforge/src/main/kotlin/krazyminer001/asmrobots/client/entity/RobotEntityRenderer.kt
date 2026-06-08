package krazyminer001.asmrobots.client.entity

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.resources.Identifier

class RobotEntityRenderer(context: EntityRendererProvider.Context) :
    LivingEntityRenderer<RobotEntity, LivingEntityRenderState, RobotEntityModel>(
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

    override fun createRenderState(): LivingEntityRenderState {
        return LivingEntityRenderState()
    }

    override fun getTextureLocation(state: LivingEntityRenderState): Identifier {
        return Identifier.fromNamespaceAndPath(AsmRobots.ID, "textures/entity/robot_entity.png")
    }
}
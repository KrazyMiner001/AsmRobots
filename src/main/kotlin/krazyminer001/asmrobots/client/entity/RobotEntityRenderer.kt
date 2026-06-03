package krazyminer001.asmrobots.client.entity

import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.EntityRenderState

class RobotEntityRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<RobotEntity, EntityRenderState>(context) {
    override fun createRenderState(): EntityRenderState {
        return EntityRenderState()
    }
}
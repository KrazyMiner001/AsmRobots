package krazyminer001.asmrobots.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier

class RobotEntityLayer(renderer: RenderLayerParent<LivingEntityRenderState, RobotEntityModel>, entityModelSet: EntityModelSet) :
    RenderLayer<LivingEntityRenderState, RobotEntityModel>(renderer) {

    val model = RobotEntityModel(entityModelSet.bakeLayer(RobotEntityModel.LAYER_LOCATION))

    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: LivingEntityRenderState,
        yRot: Float,
        xRot: Float
    ) {
        submitNodeCollector
            .order(1)
            .submitModel(
                model,
                state,
                poseStack,
                Identifier.fromNamespaceAndPath(AsmRobots.ID, "textures/entity/robot_entity.png"),
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
            )
    }
}
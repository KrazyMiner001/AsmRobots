package krazyminer001.asmrobots.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier

class RobotEntityLayer(
    renderer: RenderLayerParent<ArmedEntityRenderState, RobotEntityModel>,
    entityModelSet: EntityModelSet
) :
    RenderLayer<ArmedEntityRenderState, RobotEntityModel>(renderer) {

    val model = RobotEntityModel(entityModelSet.bakeLayer(RobotEntityModel.LAYER_LOCATION))

    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: ArmedEntityRenderState,
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

        poseStack.pushPose()
        poseStack.scale(0.5f, 0.5f, 0.5f)
        poseStack.mulPose(Axis.XP.rotationDegrees(180f))
        poseStack.mulPose(Axis.YP.rotationDegrees(180f))
        poseStack.translate(0.6f, -1.9f, -0.75f)
        state.rightHandItemState.submit(
            poseStack,
            submitNodeCollector,
            lightCoords,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor
        )
        poseStack.popPose()

        poseStack.pushPose()
        poseStack.scale(0.5f, 0.5f, 0.5f)
        poseStack.mulPose(Axis.XP.rotationDegrees(180f))
        poseStack.mulPose(Axis.YP.rotationDegrees(180f))
        poseStack.translate(-0.6f, -1.9f, -0.75f)
        state.leftHandItemState.submit(
            poseStack,
            submitNodeCollector,
            lightCoords,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor
        )
        poseStack.popPose()
    }
}
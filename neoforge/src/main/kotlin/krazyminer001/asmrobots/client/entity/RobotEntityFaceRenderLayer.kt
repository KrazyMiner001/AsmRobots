package krazyminer001.asmrobots.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Transformation
import krazyminer001.asmrobots.client.texture.FaceTextureCache
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.rendertype.RenderTypes
import org.joml.Vector3f

class RobotEntityFaceRenderLayer(renderer: RenderLayerParent<RobotEntityRenderState, RobotEntityModel>) :
    RenderLayer<RobotEntityRenderState, RobotEntityModel>(renderer) {
    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: RobotEntityRenderState,
        yRot: Float,
        xRot: Float
    ) {
        poseStack.pushPose()
        poseStack.mulPose(
            Transformation(
                Vector3f(-0.155f, 0.7175f, -0.188f),
                null,
                Vector3f(20 / 64f, 8 / 64f, 1f),
                null,
            )
        )

        submitNodeCollector.submitCustomGeometry(
            poseStack,
            RenderTypes.text(FaceTextureCache[state.face]),
        ) { pose, vertexConsumer ->
            vertexConsumer.addVertex(pose, 0.0f, 1.0f, 0f).setColor(-1).setUv(0.0f, 1.0f).setLight(lightCoords)
            vertexConsumer.addVertex(pose, 1.0f, 1.0f, 0f).setColor(-1).setUv(1.0f, 1.0f).setLight(lightCoords)
            vertexConsumer.addVertex(pose, 1.0f, 0.0f, 0f).setColor(-1).setUv(1.0f, 0.0f).setLight(lightCoords)
            vertexConsumer.addVertex(pose, 0.0f, 0.0f, 0f).setColor(-1).setUv(0.0f, 0.0f).setLight(lightCoords)
        }

        poseStack.popPose()
    }
}
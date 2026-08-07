@file:Suppress("unused", "UnusedVariable")

package krazyminer001.asmrobots.client.entity

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.resources.Identifier

class RobotEntityModel(root: ModelPart) : EntityModel<ArmedEntityRenderState>(root) {
    private val treads: ModelPart = root.getChild("treads")
    private val body: ModelPart = root.getChild("body")
    private val arm1: ModelPart = this.body.getChild("arm1")
    private val arm2: ModelPart = this.body.getChild("arm2")
    private val head: ModelPart = this.body.getChild("head")

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(Identifier.fromNamespaceAndPath(AsmRobots.ID, "robot_entity"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val treads = partdefinition.addOrReplaceChild(
                "treads",
                CubeListBuilder.create().texOffs(0, 9)
                    .addBox(-5.0f, -3.0f, -5.0f, 2.0f, 3.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(0, 9).addBox(3.0f, -3.0f, -5.0f, 2.0f, 3.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 3.1416f, 0.0f)
            )

            val body = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-3.0f, -2.0f, -4.0f, 6.0f, 1.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(28, 4).addBox(-1.0f, -4.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(0, 22).addBox(-3.0f, -9.0f, -3.0f, 6.0f, 5.0f, 6.0f, CubeDeformation(0.0f))
                    .texOffs(0, 33).addBox(-1.0f, -10.0f, -1.0f, 2.0f, 1.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 3.1416f, 0.0f)
            )

            val arm1 = body.addOrReplaceChild(
                "arm1",
                CubeListBuilder.create().texOffs(8, 33)
                    .addBox(-4.0f, -8.0f, -1.0f, 1.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(24, 16).addBox(-6.0f, -8.0f, -1.0f, 2.0f, 2.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val arm2 = body.addOrReplaceChild(
                "arm2",
                CubeListBuilder.create().texOffs(14, 33)
                    .addBox(3.0f, -8.0f, -1.0f, 1.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(24, 25).addBox(4.0f, -8.0f, -1.0f, 2.0f, 2.0f, 7.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val head = body.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(28, 0)
                    .addBox(-3.0f, -3.0f, 0.0f, 6.0f, 1.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(24, 9).addBox(-3.0f, -2.0f, -2.0f, 6.0f, 2.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, -10.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 48, 48)
        }
    }
}
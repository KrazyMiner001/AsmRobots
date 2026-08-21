package krazyminer001.asmrobots.common.entity

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.texture.FaceTexture
import net.minecraft.network.syncher.EntityDataSerializer
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModEntityDataSerializers {
    val REGISTRY = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, AsmRobots.ID)

    val FACE_STATE by REGISTRY.register("face_state") { ->
        EntityDataSerializer.forValueType(FaceTexture.STREAM_CODEC)
    }
}
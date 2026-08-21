package krazyminer001.asmrobots.client.texture

import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.texture.FaceTexture
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

object FaceTextureCache {
    private val cache: MutableMap<FaceTexture.FaceIdentifier, Identifier> = mutableMapOf()

    operator fun get(texture: FaceTexture): Identifier {
        val hash = texture.hash

        return cache.computeIfAbsent(
            hash
        ) {
            val id = AsmRobots.namespacedIdentifier("face/${hash.id}")
            Minecraft.getInstance().textureManager.register(
                id,
                texture.abstractTexture
            )

            id
        }
    }
}
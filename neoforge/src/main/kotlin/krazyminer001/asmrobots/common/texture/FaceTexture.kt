package krazyminer001.asmrobots.common.texture

import com.mojang.blaze3d.platform.NativeImage
import io.netty.buffer.ByteBuf
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ARGB

class FaceTexture(private val texture: Array<Array<Int>> = DEFAULT) {
    val abstractTexture: AbstractTexture
        get() = DynamicTexture({ "face-$hash" }, nativeImage)

    val nativeImage: NativeImage
        get() {
            val image = NativeImage(20, 8, true)
            texture.forEachIndexed { x, column ->
                column.forEachIndexed { y, pixel ->
                    image.setPixel(x, y, pixel)
                }
            }
            return image
        }

    operator fun get(x: Int, y: Int) = texture[x][y]

    val hash
        get() = FaceIdentifier(this.hashCode())

    fun clone(): Builder {
        val clonedTexture = Array(20) { texture[it].copyOf() }
        return Builder(clonedTexture)
    }

    override fun hashCode(): Int {
        return texture.contentDeepHashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FaceTexture) return false

        if (!texture.contentDeepEquals(other.texture)) return false

        return true
    }

    class Builder(val texture: Array<Array<Int>>) {
        operator fun get(x: Int, y: Int) = texture[x][y]

        fun set(x: Int, y: Int, value: Int): Builder {
            texture[x][y] = value
            return this
        }

        fun build() = FaceTexture(texture)
    }

    @JvmInline
    value class FaceIdentifier(val id: Int)

    companion object {
        val DEFAULT: Array<Array<Int>>
            get() {
                val green = ARGB.color(1f, 0x00FF00)
                val array = Array(20) { Array(8) { ARGB.black(1f) } }
                array[5][1] = green
                array[6][1] = green
                array[13][1] = green
                array[14][1] = green
                array[5][2] = green
                array[6][2] = green
                array[13][2] = green
                array[14][2] = green
                array[7][5] = green
                array[8][6] = green
                array[9][6] = green
                array[10][6] = green
                array[11][6] = green
                array[12][5] = green

                return array
            }

        val STREAM_CODEC = ByteBufCodecs.collection<ByteBuf, Int, MutableList<Int>>(
            ::ArrayList,
            ByteBufCodecs.INT,
            8
        )
            .map(List<Int>::toTypedArray, Array<Int>::toMutableList)
            .apply(ByteBufCodecs.collection<ByteBuf, Array<Int>, MutableList<Array<Int>>>(::ArrayList))
            .map(List<Array<Int>>::toTypedArray, Array<Array<Int>>::toMutableList)
            .map(::FaceTexture, FaceTexture::texture)
    }
}
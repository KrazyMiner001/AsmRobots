package krazyminer001.asmrobots.common.block

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.PrimitiveCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrElse

class RelayBlockEntity(worldPosition: BlockPos, blockState: BlockState) :
    BlockEntity(ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE, worldPosition, blockState) {
    private var data = mutableMapOf<Int, Int>()

    operator fun get(port: Int): Int = data[port] ?: 0
    operator fun set(port: Int, value: Int) {
        data[port] = value
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        data = input.read("data", DATA_CODEC).getOrElse { mutableMapOf() }
    }

    override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        data = data.filterValues { it != 0 }.toMutableMap()
        output.storeNullable("data", DATA_CODEC, data)
    }

    companion object {
        val DATA_CODEC: Codec<MutableMap<Int, Int>> = Codec.unboundedMap(PrimitiveCodec.INT, PrimitiveCodec.INT)
    }
}
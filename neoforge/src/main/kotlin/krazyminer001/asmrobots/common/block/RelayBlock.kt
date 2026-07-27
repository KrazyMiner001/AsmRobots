package krazyminer001.asmrobots.common.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class RelayBlock(properties: Properties) : BaseEntityBlock(properties) {
    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::RelayBlock)

    override fun newBlockEntity(
        blockPos: BlockPos,
        blockState: BlockState
    ): BlockEntity = RelayBlockEntity(blockPos, blockState)
}
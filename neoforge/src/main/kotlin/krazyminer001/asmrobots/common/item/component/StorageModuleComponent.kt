package krazyminer001.asmrobots.common.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

data class StorageModuleComponent(
    val itemContainerComponent: ItemContainerContents = ItemContainerContents.EMPTY,
    val size: Int
) {
    companion object {
        val CODEC: Codec<StorageModuleComponent> = RecordCodecBuilder.create {
            it.group(
                ItemContainerContents.CODEC.fieldOf("itemContainerComponent")
                    .forGetter(StorageModuleComponent::itemContainerComponent),
                Codec.INT.fieldOf("size").forGetter(StorageModuleComponent::size)
            ).apply(it, ::StorageModuleComponent)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ItemContainerContents.STREAM_CODEC, StorageModuleComponent::itemContainerComponent,
            ByteBufCodecs.INT, StorageModuleComponent::size,
            ::StorageModuleComponent
        )

        fun fromItems(items: List<ItemStack>): StorageModuleComponent {
            val containerComponent = ItemContainerContents.fromItems(items)
            return StorageModuleComponent(containerComponent, items.size)
        }
    }
}
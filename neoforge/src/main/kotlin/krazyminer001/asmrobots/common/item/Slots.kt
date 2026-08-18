package krazyminer001.asmrobots.common.item

import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import kotlin.properties.Delegates.observable

object Slots {
    fun uncappedSlot(stack: ItemStack): Slot {
        val slot = Slot(UnlimitedSimpleContainer(1), 0, 0, 0)
        slot.set(stack)
        return slot
    }

    fun cyclingUncappedSlot(count: Int, itemProvider: ItemProvider): Slot {
        val slot = Slot(UnlimitedSimpleContainer(1), 0, 0, 0)

        fun setItem(item: ItemLike?) {
            if (item != null) {
                slot.set(ItemStack(item, count))
            }
        }

        setItem(itemProvider.value)

        itemProvider += { item ->
            setItem(item)
        }
        return slot
    }

    private class UnlimitedSimpleContainer(size: Int) : SimpleContainer(size) {
        override fun getMaxStackSize(itemStack: ItemStack) = Int.MAX_VALUE
        override fun getMaxStackSize() = Int.MAX_VALUE
    }

    class ItemProvider {
        var value: ItemLike? by observable(null) { _, _, value ->
            receivers.forEach { it(value) }
        }

        val receivers: MutableList<(ItemLike?) -> Unit> = mutableListOf()

        operator fun plusAssign(receiver: (ItemLike?) -> Unit) {
            receivers.add(receiver)
        }
    }

    class Ticker {
        var value by observable(0) {_, _, value ->
            receivers.forEach { it(value) }
        }

        val receivers: MutableList<(Int) -> Unit> = mutableListOf()

        operator fun plusAssign(receiver: (Int) -> Unit) {
            receivers.add(receiver)
        }
    }
}
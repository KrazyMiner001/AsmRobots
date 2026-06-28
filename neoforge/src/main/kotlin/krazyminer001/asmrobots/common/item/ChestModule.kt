package krazyminer001.asmrobots.common.item

import com.lowdragmc.lowdraglib2.gui.factory.HeldItemUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.itemSlot
import com.lowdragmc.lowdraglib2.gui.ui.inventorySlots
import com.lowdragmc.lowdraglib2.gui.ui.layout.px
import dev.vfyjxf.taffy.style.AlignItems
import dev.vfyjxf.taffy.style.TaffyDisplay
import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper

class ChestModule(properties: Properties) : ModuleItem(properties), HeldItemUIMenuType.HeldItemUI {
    var index = 0

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val superResult = super.use(level, player, hand)
        if (superResult.consumesAction()) return superResult

        if (!player.isCrouching) return InteractionResult.PASS

        if (!level.isClientSide && player is ServerPlayer) {
            HeldItemUIMenuType.openUI(player, hand)
        }

        return InteractionResult.SUCCESS
    }

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.INDEX -> index

            IOPorts.COUNT -> itemStack
                .get(ModComponents.STORAGE_COMPONENT)
                ?.itemContainerComponent
                ?.getStackInSlot(index)?.count ?: -1

            IOPorts.ITEM -> itemStack
                .get(ModComponents.STORAGE_COMPONENT)
                ?.itemContainerComponent
                ?.getStackInSlot(index)?.item
                ?.let { BuiltInRegistries.ITEM.getId(it) } ?: -1
            else -> 0
        }
    }

    override fun setIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Int
    ) {
        when (address) {
            IOPorts.INDEX -> index = value
        }
    }

    override fun createUI(holder: HeldItemUIMenuType.HeldItemUIHolder): ModularUI {
        val root = element {
            val container = StorageModuleContainer(holder.itemStack)

            element({
                layout = {
                    display(TaffyDisplay.GRID)
                    grid {
                        templateColumns("repeat(4, min-content)")
                        templateRows("repeat(2, min-content)")
                    }
                    alignSelf(AlignItems.CENTER)
                    margin {
                        all(18.px)
                    }
                }
            }) {
                repeat(container.containerSize) {
                    itemSlot({
                        bind(VanillaContainerWrapper.of(container), it)
                    })
                }
            }

            inventorySlots { }
        }

        return ModularUI.of(UI.of(root), holder.player)
    }

    object IOPorts {
        const val INDEX = 0
        const val COUNT = 1
        const val ITEM = 2
    }
}
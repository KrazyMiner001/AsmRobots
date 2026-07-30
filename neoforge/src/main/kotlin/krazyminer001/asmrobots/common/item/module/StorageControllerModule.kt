package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.component.ModComponents
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.Transaction
import kotlin.math.min

class StorageControllerModule(properties: Properties) : ModuleItem(properties) {
    var sourceContainerIndex = 0
    var destinationContainerIndex = 0
    var sourceItemIndex = 0
    var destinationItemIndex = 0
    var itemsMoved = -1

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.SOURCE_CONTAINER -> sourceContainerIndex
            IOPorts.DESTINATION_CONTAINER -> destinationContainerIndex
            IOPorts.SOURCE_ITEM -> sourceItemIndex
            IOPorts.DESTINATION_ITEM -> destinationItemIndex
            IOPorts.MOVE -> itemsMoved
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
            IOPorts.SOURCE_CONTAINER -> sourceContainerIndex = value
            IOPorts.DESTINATION_CONTAINER -> destinationContainerIndex = value
            IOPorts.SOURCE_ITEM -> sourceItemIndex = value
            IOPorts.DESTINATION_ITEM -> destinationItemIndex = value
            IOPorts.MOVE -> {
                if (value < 0) return
                val source = robotEntity.modulesInventory.getItem(sourceContainerIndex)
                val destination = robotEntity.modulesInventory.getItem(destinationContainerIndex)

                if (
                    !source.has(ModComponents.STORAGE_COMPONENT)
                    || !destination.has(ModComponents.STORAGE_COMPONENT)
                    ) return

                val sourceHandler = VanillaContainerWrapper.of(StorageModuleContainer(source))
                val destinationHandler = VanillaContainerWrapper.of(StorageModuleContainer(destination))

                Transaction.openRoot().use {
                    val sourceResource = sourceHandler.getResource(sourceItemIndex)
                    if (sourceResource.isEmpty) return@use

                    val sourceAmount = sourceHandler.getAmountAsInt(sourceItemIndex)
                    val inserted = destinationHandler.insert(destinationItemIndex, sourceResource, min(sourceAmount, value), it)

                    val extracted = sourceHandler.extract(sourceItemIndex, sourceResource, inserted, it)

                    if (extracted == inserted) {
                        it.commit()
                        itemsMoved = extracted
                    } else {
                        itemsMoved = -1
                    }
                }
            }
        }
    }

    object IOPorts {
        const val SOURCE_CONTAINER = 0
        const val DESTINATION_CONTAINER = 1
        const val SOURCE_ITEM = 2
        const val DESTINATION_ITEM = 3
        const val MOVE = 4
    }
}
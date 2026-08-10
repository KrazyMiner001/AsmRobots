package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.block.ModBlockEntities
import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.module.AdvancedNetworkingModule.IOPort.Companion.toIOPort
import krazyminer001.asmrobots.common.item.module.AdvancedNetworkingModule.MemoryIdentifier.Companion.toMemoryIdentifier
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

class AdvancedNetworkingModule(properties: Properties) : MemoryMappedModuleItem(properties) {
    val relayPos = BlockPos.MutableBlockPos(0, 0, 0)
    var portOffset = 0

    override fun getMappedMemory(
        identifier: Int,
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Byte {
        return when (address.toMemoryIdentifier()) {
            MemoryIdentifier.NETWORKED_MEMORY -> {
                val relay = robotEntity
                    .level()
                    .getBlockEntity(relayPos, ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE)
                    .getOrNull() ?: return 0

                relay[address]
            }
            null -> 0
        }
    }

    override fun setMappedMemory(
        identifier: Int,
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Byte
    ) {
        when (address.toMemoryIdentifier()) {
            MemoryIdentifier.NETWORKED_MEMORY -> {
                val relay = robotEntity
                    .level()
                    .getBlockEntity(relayPos, ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE)
                    .getOrNull() ?: return

                relay[address] = value
            }
            null -> {}
        }
    }

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address.toIOPort()) {
            IOPort.RELAY_ADDRESS_X -> relayPos.x
            IOPort.RELAY_ADDRESS_Y -> relayPos.y
            IOPort.RELAY_ADDRESS_Z -> relayPos.z
            IOPort.PORT_OFFSET -> portOffset
            null -> 0
        }
    }

    override fun setIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity,
        value: Int
    ) {
        when (address.toIOPort()) {
            IOPort.RELAY_ADDRESS_X -> relayPos.x = value
            IOPort.RELAY_ADDRESS_Y -> relayPos.y = value
            IOPort.RELAY_ADDRESS_Z -> relayPos.z = value
            IOPort.PORT_OFFSET -> portOffset = value
            null -> {}
        }
    }

    enum class IOPort {
        RELAY_ADDRESS_X,
        RELAY_ADDRESS_Y,
        RELAY_ADDRESS_Z,
        PORT_OFFSET;

        companion object {
            fun Int.toIOPort(): IOPort? = IOPort.entries.getOrNull(this)
        }
    }

    enum class MemoryIdentifier {
        NETWORKED_MEMORY;

        companion object {
            fun Int.toMemoryIdentifier(): MemoryIdentifier? = MemoryIdentifier.entries.getOrNull(this)
        }
    }
}
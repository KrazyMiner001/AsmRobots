package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.asm.Program
import krazyminer001.asmrobots.common.block.ModBlockEntities
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

class NetworkingModule(properties: Properties) : ModuleItem(properties) {
    var relayPos: BlockPos.MutableBlockPos = BlockPos.MutableBlockPos(0, 0, 0)
    var port: Int = 0
    val subscribedPorts = mutableListOf<Pair<Int, Int>>()
    var subscribedPortIndex = 0

    override fun tick(program: Program, portOffset: Int, level: ServerLevel) {
        val relay = level.getBlockEntity(relayPos, ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE).getOrNull() ?: return

        subscribedPorts.forEachIndexed { index, (port, oldValue) ->
            val value = relay[port]
            if (value != oldValue) {
                program.interrupt(portOffset + index)
            }

            subscribedPorts[index] = Pair(port, oldValue)
        }
    }

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.PORT -> port
            IOPorts.VALUE -> {
                val relay = robotEntity
                    .level()
                    .getBlockEntity(relayPos, ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE)
                    .getOrNull() ?: return -1

                relay[port]
            }

            IOPorts.RELAY_ADDRESS_X -> relayPos.x
            IOPorts.RELAY_ADDRESS_Y -> relayPos.y
            IOPorts.RELAY_ADDRESS_Z -> relayPos.z
            IOPorts.SUBSCRIBED_PORT_INDEX -> subscribedPortIndex
            IOPorts.SUBSCRIBED_PORT_VALUE -> subscribedPorts.getOrNull(subscribedPortIndex)?.first ?: -1
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
            IOPorts.PORT -> port = value
            IOPorts.VALUE -> {
                val relay = robotEntity
                    .level()
                    .getBlockEntity(relayPos, ModBlockEntities.RELAY_BLOCK_ENTITY_TYPE)
                    .getOrNull() ?: return

                relay[port] = value
            }

            IOPorts.RELAY_ADDRESS_X -> relayPos.x = value
            IOPorts.RELAY_ADDRESS_Y -> relayPos.y = value
            IOPorts.RELAY_ADDRESS_Z -> relayPos.z = value
            IOPorts.SUBSCRIBED_PORT_INDEX -> subscribedPortIndex = value
            IOPorts.SUBSCRIBED_PORT_VALUE -> {
                if (subscribedPortIndex < subscribedPorts.size) {
                    subscribedPorts[subscribedPortIndex] = Pair(value, 0)
                } else if (subscribedPortIndex == subscribedPorts.size && subscribedPorts.size <= 16) {
                    subscribedPorts.add(Pair(value, 0))
                }
            }

            IOPorts.REMOVE_SUBSCRIBED_PORT -> {
                if (subscribedPortIndex < subscribedPorts.size) {
                    subscribedPorts.removeAt(subscribedPortIndex)
                    subscribedPortIndex--
                } else {
                    subscribedPorts.removeLast()
                    subscribedPortIndex = subscribedPorts.lastIndex
                }
            }
        }
    }

    object IOPorts {
        const val PORT = 0
        const val VALUE = 1
        const val RELAY_ADDRESS_X = 2
        const val RELAY_ADDRESS_Y = 3
        const val RELAY_ADDRESS_Z = 4
        const val SUBSCRIBED_PORT_INDEX = 5
        const val SUBSCRIBED_PORT_VALUE = 6
        const val REMOVE_SUBSCRIBED_PORT = 7
    }
}
package krazyminer001.asmrobots.common.item.module

import krazyminer001.asmrobots.common.entity.RobotEntity
import krazyminer001.asmrobots.common.item.container.StorageModuleContainer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.Transaction
import kotlin.math.max

class StorageBlockInterfaceModule(properties: Properties) : ModuleItem(properties) {
    var targetIndex = -1
    var lastTransferredAmount = -1

    override fun getIOPort(
        address: Int,
        itemStack: ItemStack,
        robotEntity: RobotEntity
    ): Int {
        return when (address) {
            IOPorts.TARGET_INDEX -> targetIndex
            IOPorts.PULL -> lastTransferredAmount
            IOPorts.CONTAINER_ITEM -> {
                val level = robotEntity.level()
                val hitResult = raycastBlock(robotEntity, itemStack)

                if (hitResult.type == HitResult.Type.BLOCK) {
                    val container = level.getCapability(Capabilities.Item.BLOCK, hitResult.blockPos, null)
                    container?.getResource(targetIndex)?.item?.let {
                        BuiltInRegistries.ITEM.getId(it)
                    } ?: -1
                } else {
                    -1
                }
            }

            IOPorts.CONTAINER_STACK_SIZE -> {
                val level = robotEntity.level()
                val hitResult = raycastBlock(robotEntity, itemStack)

                if (hitResult.type == HitResult.Type.BLOCK) {
                    val container = level.getCapability(Capabilities.Item.BLOCK, hitResult.blockPos, null)
                    container?.getAmountAsInt(targetIndex) ?: -1
                } else {
                    -1
                }
            }

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
            IOPorts.TARGET_INDEX -> targetIndex = value
            IOPorts.PULL -> {
                val level = robotEntity.level()
                val hitResult = raycastBlock(robotEntity, itemStack)

                if (hitResult.type == HitResult.Type.BLOCK) {
                    val pos = hitResult.blockPos
                    val storageBlock = level.getCapability(Capabilities.Item.BLOCK, pos, null) ?: return

                    val robotStorageHandler = VanillaContainerWrapper.of(StorageModuleContainer(itemStack))

                    fun transferItems(sourceHandler: ResourceHandler<ItemResource>, destinationHandler: ResourceHandler<ItemResource>, amount: Int) {
                        Transaction.openRoot().use { tx ->
                            val resource = sourceHandler.getResource(targetIndex)
                            if (resource.isEmpty) return@use

                            val sourceAmount = sourceHandler.getAmountAsInt(targetIndex)
                            val inserted = destinationHandler.insert(resource, sourceAmount.coerceAtMost(amount), tx)

                            val extracted = sourceHandler.extract(targetIndex, resource, inserted, tx)

                            if (inserted == extracted) {
                                tx.commit()
                                lastTransferredAmount = inserted
                            } else {
                                lastTransferredAmount = -1
                            }
                        }
                    }

                    if (value > 0) {
                        transferItems(storageBlock, robotStorageHandler, value)
                    } else if (value < 0) {
                        transferItems(robotStorageHandler, storageBlock, -value)
                    } else {
                        lastTransferredAmount = 0
                    }
                }
            }
        }
    }

    private fun raycastBlock(
        robotEntity: RobotEntity,
        itemStack: ItemStack
    ): BlockHitResult {
        val level = robotEntity.level()
        val attackRange = robotEntity.getAttackRangeWith(itemStack)
        val look: Vec3 = robotEntity.headLookAngle
        val eyePosition: Vec3 = robotEntity.eyePosition
        val from = eyePosition.add(look.scale(attackRange.effectiveMinRange(robotEntity).toDouble()))
        val movementComponent: Double = robotEntity.knownMovement.dot(look)
        val to = eyePosition.add(look.scale(attackRange.effectiveMaxRange(robotEntity) + max(0.0, movementComponent)))

        val hitResult =
            level.clip(ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, robotEntity))
        return hitResult
    }

    object IOPorts {
        const val TARGET_INDEX = 0
        const val PULL = 1
        const val CONTAINER_ITEM = 2
        const val CONTAINER_STACK_SIZE = 3
    }
}
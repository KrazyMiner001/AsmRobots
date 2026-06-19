package krazyminer001.asmrobots.common.entity

import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
import com.lowdragmc.lowdraglib2.gui.sync.rpc.rpcEvent
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.button
import com.lowdragmc.lowdraglib2.gui.ui.layout.px
import krazyminer001.asmrobots.common.asm.*
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import krazyminer001.asmrobots.common.ui.elements.assemblyEditor
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.control.LookControl
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.ceil

class RobotEntity(type: EntityType<RobotEntity> = ModEntities.ROBOT_ENTITY, level: Level) : Mob(type, level),
    IContainerUIHolder, ProgramCallback {

    init {
        this.lookControl = object : LookControl(this) {
            override fun resetXRotOnTick() = false

            override fun tick() {
                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, (this.mob as RobotEntity).targetYaw, 10f)
                this.mob.yBodyRot = this.mob.yHeadRot
                this.mob.yRot = this.mob.yHeadRot
                this.mob.xRot = this.rotateTowards(this.mob.xRot, (this.mob as RobotEntity).targetPitch, 10f)

                this.clampHeadRotationToBody()
            }
        }
    }

    var code: String
        get() = entityData.get(CODE_DATA)
        set(value) = entityData.set(CODE_DATA, value)

    var program: Program? = null

    var isBreaking = false
    var shouldNotifyBump = false

    val messagePrefix: MutableComponent
        get() = Component.literal("[").append(this.displayName).append("] ")

    var velocity: Float = 0f
    var targetYaw: Float = 0f
    var targetPitch: Float = 0f

    var blockBreakProgress: Pair<BlockPos, Int>? = null

    override fun getMainArm(): HumanoidArm {
        return HumanoidArm.LEFT
    }

    override fun interact(player: Player, hand: InteractionHand, location: Vec3): InteractionResult {
        val superValue = super.interact(player, hand, location)
        if (superValue.consumesAction()) return superValue

        player.openMenu(object : MenuProvider {
            override fun getDisplayName(): Component {
                return Component.literal("Robot")
            }

            override fun createMenu(containerId: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
                return ModularUIContainerMenu(ModMenuTypes.ROBOT_UI, containerId, inventory, this@RobotEntity)
            }

            override fun writeClientSideData(menu: AbstractContainerMenu, buffer: RegistryFriendlyByteBuf) {
                buffer.writeUUID(this@RobotEntity.uuid)
            }
        })

        return InteractionResult.SUCCESS
    }

    override fun createUI(player: Player): ModularUI {
        val root = element {
            var clientCode = code.split('\n').toTypedArray()
            assemblyEditor({
                lines(*clientCode)
                linesResponder = { clientCode = it }
                layout = { size(300.px, 200.px) }
            })

            button({
                val rpcEvent = element.rpcEvent(::code::set)
                onClick = {
                    rpcEvent.send(clientCode.joinToString("\n"))
                }
                text("Save")
            })
            button({
                onServerClick = {
                    val text = Assembler.lex(code).map { Assembler.parse(it) }.let { lines ->
                        if (lines.any { it.isFailure })
                            AsmResult.Failure(
                                AsmError
                                .ParseError
                                .ParseErrors(
                                    lines
                                        .mapIndexed { index, result -> Pair(result, index) }
                                        .filter { it.first.isFailure }
                                        .map { (result, index) -> Pair(result.errorValue!!, index) }
                                ))
                        else
                            lines.map { it.successValue!! }.asSuccess()
                    }.fold({
                        it.joinToString()
                    }, {
                        it.text
                    })
                    player.sendSystemMessage(Component.literal(text))
                }
            })
            button({
                text("Execute")
                onServerClick = clickHandler@{
                    val (code, labels) = Assembler.assemble(
                        Assembler.lex(code).map { Assembler.parse(it).getOrElse { return@clickHandler } }
                    ).getOrElse { return@clickHandler }
                    program = Program(this@RobotEntity)
                    program?.initMemoryAndLabels(code, labels)
                }
            })
        }

        return ModularUI(UI.of(root), player)
    }

    override fun isStillValid(player: Player): Boolean {
        return true
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(CODE_DATA, "")
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        code = input.getStringOr("code", "")
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.putString("code", code)
    }

    override fun shouldShowName(): Boolean {
        return super.shouldShowName()
    }

    override fun halt() {
        program = null
    }

    override fun tick() {
        super.tick()
        try {
            program?.step()
        } catch (_: Throwable) {
            program = null
        }
        if (onGround()) {
            deltaMovement = deltaMovement.add(getInputVector(Vec3(0.0, 0.0, 1.0), velocity, yHeadRot))

            if (horizontalCollision && shouldNotifyBump) {
                program?.interrupt(Interrupts.BUMP)
            }
        }

        if (isBreaking) {
            val hitResult = this
                .getAttackRangeWith(ItemStack.EMPTY)
                .getClosesetHit(this, 0f) { true }

            when (hitResult) {
                is BlockHitResult -> {
                    var blockBreakProgress = blockBreakProgress
                    blockBreakProgress =
                        if (blockBreakProgress == null || blockBreakProgress.first != hitResult.blockPos) {
                            Pair(hitResult.blockPos, 1)
                        } else {
                            blockBreakProgress.copy(second = blockBreakProgress.second + 1)
                        }

                    val block = level().getBlockState(hitResult.blockPos)
                    val harvestMultiplier = if (block.requiresCorrectToolForDrops()) 100 else 30
                    val totalTicks = ceil(harvestMultiplier * block.block.defaultDestroyTime()).toInt()

                    level().destroyBlockProgress(this.id, hitResult.blockPos,
                        (10 * blockBreakProgress.second.toFloat() / totalTicks).toInt()
                    )
                    if (blockBreakProgress.second >= totalTicks) {
                        level().destroyBlock(hitResult.blockPos, true, this)
                        this.blockBreakProgress = null
                    } else {
                        this.blockBreakProgress = blockBreakProgress
                    }
                }
                is EntityHitResult -> {
                    val level = level()
                    if (level is ServerLevel) {
                        hitResult.entity.hurtServer(
                            level,
                            this.damageSources().mobAttack(this),
                            1f
                        )
                    }
                }
            }
        }
    }

    override fun get(ioAddress: Int): Int {
        return when (ioAddress) {
            IOPorts.VELOCITY -> velocity.toBits()
            IOPorts.FEET_BLOCK -> BuiltInRegistries.BLOCK
                .getId(level().getBlockState(blockPosition().offset(0, -1, 0)).block)
            IOPorts.ATTACK -> if (isBreaking) 1 else 0
            IOPorts.NOTIFY_BUMP -> if (shouldNotifyBump) 1 else 0
            IOPorts.YAW -> yHeadRot.toInt()
            IOPorts.PITCH -> xRot.toInt()
            else -> 0
        }
    }

    override fun set(ioAddress: Int, value: Int) {
        when (ioAddress) {
            IOPorts.VELOCITY -> velocity = Float.fromBits(value).coerceIn(-1f..1f) / 20
            IOPorts.PRINT_INT -> {
                val level = level()
                if (level is ServerLevel) {
                    level.server.playerList
                        .broadcastSystemMessage(messagePrefix.append(value.toString()), false)
                }
            }
            IOPorts.PRINT_FLOAT -> {
                val level = level()
                if (level is ServerLevel) {
                    level.server.playerList
                        .broadcastSystemMessage(messagePrefix.append(Float.fromBits(value).toString()), false)
                }
            }
            IOPorts.ATTACK -> isBreaking = value != 0
            IOPorts.NOTIFY_BUMP -> shouldNotifyBump = value != 0
            IOPorts.YAW -> targetYaw = value.toFloat() % 360
            IOPorts.PITCH -> targetPitch = value.toFloat() % 360
        }
    }

    companion object {
        val CODE_DATA: EntityDataAccessor<String> =
            SynchedEntityData.defineId(RobotEntity::class.java, EntityDataSerializers.STRING)
    }

    object IOPorts {
        const val VELOCITY = 1
        const val PRINT_INT = 2
        const val PRINT_FLOAT = 3
        const val FEET_BLOCK = 4
        const val ATTACK = 5
        const val NOTIFY_BUMP = 6
        const val YAW = 7
        const val PITCH = 8
    }

    object Interrupts {
        const val BUMP = 0
    }
}


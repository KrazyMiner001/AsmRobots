package krazyminer001.asmrobots.common.entity

import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
import com.lowdragmc.lowdraglib2.gui.sync.rpc.rpcEvent
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.button
import com.lowdragmc.lowdraglib2.gui.ui.elements.codeeditor.codeEditor
import com.lowdragmc.lowdraglib2.gui.ui.layout.px
import krazyminer001.asmrobots.common.asm.*
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import krazyminer001.asmrobots.common.ui.elements.AssemblyEditor
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3

class RobotEntity(type: EntityType<RobotEntity> = ModEntities.ROBOT_ENTITY, level: Level) : LivingEntity(type, level),
    IContainerUIHolder, ProgramCallback {

    var code: String
        get() = entityData.get(CODE_DATA)
        set(value) = entityData.set(CODE_DATA, value)

    var program: Program? = null

    val messagePrefix: MutableComponent
        get() = Component.literal("[").append(this.displayName).append("] ")

    var velocity: Int = 0
    var targetRotation: Double = 0.0
    var rotationSteps: Int = 0
    fun lerpRotation(targetRotation: Double, steps: Int) {
        rotationSteps = steps
        this.targetRotation = targetRotation
    }
    fun stepRotation() {
        if (rotationSteps > 0) {
            yRot = Mth.rotLerp(1.0 / rotationSteps, yRot.toDouble(), targetRotation).toFloat() % 360
            rotationSteps--
        }
    }

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
            codeEditor({
                lines(*clientCode)
                linesResponder = { clientCode = it }
                layout = { size(300.px, 200.px) }
                language = AsmLanguageDefinition
                styleManager = AsmStyleManager
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
                    val text = lex(code).fold({
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
                    val (code, labels) = assemble(lex(code).getOrElse { return@clickHandler }).getOrElse { return@clickHandler }
                    program = Program(this@RobotEntity)
                    program?.initMemoryAndLabels(code, labels)
                }
            })
        }
        root.addChild(AssemblyEditor("Mrowwww\nmroooow\nmrump"))

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
        stepRotation()
        try {
            program?.step()
        } catch (_: Throwable) {
            program = null
        }
        if (onGround()) {
            addDeltaMovement(
                Vec3.directionFromRotation(rotationVector)
                    .normalize()
                    .scale(
                        velocity
                            .toDouble()
                            .div(256)
                            .coerceIn(-1.0..1.0) / 20
                    )
            )
        }
    }

    override fun get(ioAddress: Int): Int {
        return when(ioAddress) {
            0 -> yRot.toInt()
            1 -> velocity
            else -> 0
        }
    }

    override fun set(ioAddress: Int, value: Int) {
        when (ioAddress) {
            0 -> lerpRotation(value.toDouble(), 10)
            1 -> velocity = value
            2 -> {
                val level = level()
                if (level is ServerLevel) {
                    level.server.playerList
                        .broadcastSystemMessage(messagePrefix.append(value.toString()), false)
                }
            }
            3 -> {
                val level = level()
                if (level is ServerLevel) {
                    level.server.playerList
                        .broadcastSystemMessage(messagePrefix.append(Float.fromBits(value).toString()), false)
                }
            }
        }
    }

    companion object {
        val CODE_DATA: EntityDataAccessor<String> =
            SynchedEntityData.defineId(RobotEntity::class.java, EntityDataSerializers.STRING)
    }
}
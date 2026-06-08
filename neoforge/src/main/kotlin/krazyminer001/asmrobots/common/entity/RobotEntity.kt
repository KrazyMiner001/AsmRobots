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
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
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

    var counter = 0

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
                    val text = lex(code).fold(
                        {
                            it.toString()
                        },
                        { throwable ->
                            if (throwable is ParseErrors) {
                                throwable.parseErrors.joinToString { "Error on line ${it.lineNumber}: ${it.cause?.message}" }
                            } else {
                                "Unexpected exception: $throwable"
                            }
                        }
                    )
                    player.sendSystemMessage(Component.literal(text))
                }
            })
            button({
                text("Execute")
                onServerClick = clickHandler@{
                    val code = lex(code).getOrElse { return@clickHandler }
                    program = Program(code, this@RobotEntity)
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
        if (counter++ % 10 == 0) {
            counter = 0
            program?.step()
        }
    }

    override fun get(ioAddress: Int): Int {
        return 0
    }

    override fun set(ioAddress: Int, value: Int) {
        val level = level()
        if (ioAddress == 0 && level is ServerLevel) {
            level.server.logChatMessage(
                Component.literal("You send number: $value"),
                ChatType.bind(ChatType.CHAT, this),
                null
            )
        }
    }

    companion object {
        val CODE_DATA: EntityDataAccessor<String> =
            SynchedEntityData.defineId(RobotEntity::class.java, EntityDataSerializers.STRING)
    }
}
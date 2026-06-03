package krazyminer001.asmrobots.common.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.factory.IContainerUIHolder
import com.lowdragmc.lowdraglib2.gui.factory.LDMenuTypes
import com.lowdragmc.lowdraglib2.gui.factory.PlayerUIMenuType
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.button
import krazyminer001.asmrobots.common.ui.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
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
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class RobotEntity(type: EntityType<RobotEntity> = ModEntities.ROBOT_ENTITY, level: Level) : LivingEntity(type, level), IContainerUIHolder {
    override fun getMainArm(): HumanoidArm {
        return HumanoidArm.LEFT
    }

    override fun getHitbox(): AABB {
        return AABB.of(BoundingBox(-1, 0, -1, 1, 1, 1))
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
        val root = element({}) {
            button({text("Meow")}) {  }
        }

        return ModularUI(UI.of(root), player)
    }

    override fun isStillValid(player: Player): Boolean {
        return true
    }
}
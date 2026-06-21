package krazyminer001.asmrobots.common.ui

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
import krazyminer001.asmrobots.common.AsmRobots
import krazyminer001.asmrobots.common.entity.RobotEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModMenuTypes {
    val REGISTRY = DeferredRegister.create(Registries.MENU, AsmRobots.ID)

    val ROBOT_UI: MenuType<ModularUIContainerMenu> by REGISTRY.register("robot_ui") { ->
        IMenuTypeExtension.create { containerId, inventory, buf ->
            val uuid = buf.readUUID()
            ModularUIContainerMenu(
                ROBOT_UI,
                containerId,
                inventory,
                inventory.player.level().getEntity(uuid)
                        as? RobotEntity ?: throw IllegalArgumentException("No robot entity found with uuid: $uuid")
            )
        }
    }
}
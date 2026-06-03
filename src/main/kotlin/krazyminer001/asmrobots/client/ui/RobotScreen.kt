package krazyminer001.asmrobots.client.ui

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolderMenu
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
import com.lowdragmc.lowdraglib2.gui.ui.ModularUIClientAccess
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class RobotScreen(menu: ModularUIContainerMenu, inventory: Inventory, title: Component) :
    AbstractContainerScreen<ModularUIContainerMenu>(menu, inventory, title) {

    override fun init() {
        modularMenu?.modularUI?.let { ModularUIClientAccess.getWidget(it) }?.let { addRenderableWidget(it) }
        modularMenu?.modularUI?.let { width = it.width.toInt() }
        modularMenu?.modularUI?.let { height = it.height.toInt() }
        super.init()
    }

    private val modularMenu: IModularUIHolderMenu?
        get() {
            @Suppress("CAST_NEVER_SUCCEEDS")
            return menu as? IModularUIHolderMenu
        }
}
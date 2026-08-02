package krazyminer001.asmrobots.common.world

import krazyminer001.asmrobots.common.AsmRobots
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.TicketType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModTicketTypes {
    val REGISTRY = DeferredRegister.create(BuiltInRegistries.TICKET_TYPE, AsmRobots.ID)

    val ROBOT_LOADER by REGISTRY.register("robot_loader") { ->
        TicketType(
            10L,
            TicketType.FLAG_LOADING
                    or TicketType.FLAG_KEEP_DIMENSION_ACTIVE
                    or TicketType.FLAG_PERSIST
                    or TicketType.FLAG_SIMULATION

        )
    }
}
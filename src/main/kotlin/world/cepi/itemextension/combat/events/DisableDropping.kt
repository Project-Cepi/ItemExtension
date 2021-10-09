package world.cepi.itemextension.combat.events

import net.minestom.server.event.item.ItemDropEvent
import world.cepi.kstom.Manager

object DisableDropping {

    fun register(event: ItemDropEvent) = with(event) {
        // Crews handle item dropping
        if (Manager.extension.hasExtension("Crews")) return@with

        isCancelled = true
    }
}
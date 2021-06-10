package world.cepi.itemextension.combat.events

import net.minestom.server.event.item.ItemDropEvent

object DisableDropping {

    fun register(event: ItemDropEvent) = with(event) {
        isCancelled = true
    }
}
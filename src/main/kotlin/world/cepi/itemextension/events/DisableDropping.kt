package world.cepi.itemextension.events

import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import world.cepi.kstom.addEventCallback

object DisableDropping {

    fun load(player: Player) {
        player.addEventCallback(ItemDropEvent::class) {
            isCancelled = true
        }
    }
}
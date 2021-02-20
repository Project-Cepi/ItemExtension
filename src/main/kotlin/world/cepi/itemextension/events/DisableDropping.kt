package world.cepi.itemextension.events

import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import world.cepi.kstom.addEventCallback

object DisableDropping : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback(ItemDropEvent::class) {
            isCancelled = true
        }
    }
}
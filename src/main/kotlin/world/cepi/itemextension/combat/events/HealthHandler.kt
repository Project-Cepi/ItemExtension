package world.cepi.itemextension.combat.events

import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityDamageEvent
import world.cepi.itemextension.Handler
import world.cepi.kstom.addEventCallback

object HealthHandler : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback<EntityDamageEvent> {
            damage /= 20 // each heart = 20 health
        }
    }
}
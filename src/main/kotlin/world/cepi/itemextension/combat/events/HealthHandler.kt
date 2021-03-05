package world.cepi.itemextension.combat.events

import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityDamageEvent
import world.cepi.itemextension.Handler

object HealthHandler : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback(EntityDamageEvent::class.java) {
            it.damage = it.damage/20 // each heart = 20 health
        }
    }
}
package world.cepi.itemextension.combat.events

import net.minestom.server.event.entity.EntityDamageEvent

object HealthHandler {

    fun register(event: EntityDamageEvent) = with(event) {
        damage /= 20 // each heart = 20 health
    }
}
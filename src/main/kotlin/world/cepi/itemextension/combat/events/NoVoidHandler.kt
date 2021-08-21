package world.cepi.itemextension.combat.events

import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.event.entity.EntityDamageEvent

object NoVoidHandler {

    fun register(event: EntityDamageEvent): Unit = with(event) {

        // Only void damage type
        if (damageType != DamageType.VOID) return

        isCancelled = true

        if (entity !is Player) {
            entity.remove()
            return
        }

        entity.teleport((entity as Player).respawnPoint)

    }


}
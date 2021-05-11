package world.cepi.itemextension.combat.events

import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.event.entity.EntityDamageEvent
import world.cepi.itemextension.Handler
import world.cepi.kstom.addEventCallback

object NoVoidHandler : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback<EntityDamageEvent> {

            // Only void damage type
            if (damageType != DamageType.VOID) return@addEventCallback

            isCancelled = true

            if (entity !is Player) {
                entity.remove()
                return@addEventCallback
            }

            entity.teleport((entity as Player).respawnPoint)

        }
    }

}
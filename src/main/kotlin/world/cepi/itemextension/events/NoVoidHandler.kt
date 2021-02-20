package world.cepi.itemextension.events

import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.event.entity.EntityDamageEvent

object NoVoidHandler : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback(EntityDamageEvent::class.java) { entityDamageEvent ->

            if (entityDamageEvent.entity !is Player) {
                return@addEventCallback
            }

            val player = entityDamageEvent.entity as Player

            if (entityDamageEvent.damageType == DamageType.VOID) {
                player.teleport(player.respawnPoint)
                entityDamageEvent.isCancelled = true
            }
        }
    }

}
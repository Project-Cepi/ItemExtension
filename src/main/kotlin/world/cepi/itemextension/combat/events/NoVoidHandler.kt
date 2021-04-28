package world.cepi.itemextension.combat.events

import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.event.entity.EntityDamageEvent
import world.cepi.itemextension.Handler
import world.cepi.kstom.addEventCallback

object NoVoidHandler : Handler {

    override fun register(playerInit: Player) {
        playerInit.addEventCallback<EntityDamageEvent> {

            if (entity !is Player) {
                return@addEventCallback
            }

            val player = entity as Player

            if (damageType == DamageType.VOID) {
                player.teleport(player.respawnPoint)
                isCancelled = true
            }
        }
    }

}
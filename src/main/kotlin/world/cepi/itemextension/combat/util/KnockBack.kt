package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity
import net.minestom.server.utils.Vector


fun applyKnockback(hitted: Entity, attacker: Entity) {
    val velocity: Vector = attacker.position.direction.multiply(6)
    velocity.y = 4.0
    hitted.velocity = velocity
}

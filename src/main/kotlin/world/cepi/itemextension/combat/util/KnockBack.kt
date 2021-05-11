package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity


fun applyKnockback(hitted: Entity, attacker: Entity) {
    hitted.velocity = attacker.position.direction.multiply(6).apply {
        y = 4.0
    }
}

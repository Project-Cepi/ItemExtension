package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity


fun applyKnockback(hitted: Entity, attacker: Entity, extra: Double = 1.0) {
    hitted.velocity = attacker.position.direction.multiply(6 * extra).apply {
        y = 4.0 * extra
    }
}

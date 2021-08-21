package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity
import kotlin.math.cos
import kotlin.math.sin


fun applyKnockback(hitted: Entity, attacker: Entity, extra: Float = 1.0f) {
    hitted.takeKnockback(
        0.4f * extra,
        sin(attacker.position.yaw() * 0.017453292),
        -cos(attacker.position.yaw() * 0.017453292)
    )
}

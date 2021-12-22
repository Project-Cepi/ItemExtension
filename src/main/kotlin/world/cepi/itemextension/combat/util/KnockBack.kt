package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity
import kotlin.math.cos
import kotlin.math.sin

fun Entity.applyKnockback(attacker: Entity, extra: Float = 0f) {

    takeKnockback(1 + extra,
        sin(attacker.position.yaw() * 0.017453292F).toDouble(),
        -cos(attacker.position.yaw() * 0.017453292F).toDouble()
    );

}

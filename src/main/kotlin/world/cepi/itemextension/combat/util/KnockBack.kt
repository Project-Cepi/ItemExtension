package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val verticalKnockback = 0.4 * 20
const val horizontalKnockback = 0.4 * 20
const val extraHorizontalKnockback = 0.3 * 20
const val extraVerticalKnockback = 0.1 * 20
const val limitVerticalKnockback = 0.4 * 20

fun Entity.applyKnockback(attacker: Entity, extra: Float = 0f) {

    val distanceX = attacker.position.x() - position.x()
    val distanceY = attacker.position.z() - position.z()

    val magnitude = attacker.position.distance(position)
    var knockback = extra

    if (attacker.isSprinting) knockback += 1.25f

    velocity = velocity
        .withX { x -> (x / 2) - (distanceX / magnitude * horizontalKnockback) }
        .withY { y -> (y / 2) + verticalKnockback }
        .withZ { z -> (z / 2) - (distanceY / magnitude * horizontalKnockback) }
        .add(
            -sin(attacker.position.yaw() * PI / 180.0f) * knockback * extraHorizontalKnockback,
            if (knockback != 0f) extraVerticalKnockback else 0.0,
            cos(attacker.position.yaw() * PI / 180.0f) * knockback * extraHorizontalKnockback
        ).withY { if (it > limitVerticalKnockback) limitVerticalKnockback else it }

}

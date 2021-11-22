package world.cepi.itemextension.combat.util

import net.minestom.server.entity.Entity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun Entity.applyKnockback(attacker: Entity, extra: Float = 1.0f) {
    val verticalKnockback = 0.4 * 20
    val horizontalKnockback = 0.4 * 20
    val extraHorizontalKnockback = 0.3 * 20
    val extraVerticalKnockback = 0.1 * 20
    val limitVerticalKnockback = 0.4 * 20

    val distanceX = attacker.position.x() - position.x()
    val distanceY = attacker.position.z() - position.z()

    val magnitude = attacker.position.distance(position)
    var knockback = extra

    if (attacker.isSprinting) knockback += 1.25f

    var newVelocity = velocity
        .withX { x -> (x / 2) - (distanceX / magnitude * horizontalKnockback) }
        .withY { y -> (y / 2) + verticalKnockback }
        .withZ { z -> (z / 2) - (distanceY / magnitude * horizontalKnockback) }

    if (knockback > 0) newVelocity = newVelocity.add(
        -sin(attacker.position.yaw() * PI / 180.0f) * knockback * extraHorizontalKnockback,
        extraVerticalKnockback,
        cos(attacker.position.yaw() * PI / 180.0f) * knockback * extraHorizontalKnockback
    )

    if (newVelocity.y() > limitVerticalKnockback) newVelocity = newVelocity.withY(limitVerticalKnockback)

    velocity = newVelocity

}

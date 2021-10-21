package world.cepi.itemextension.combat.util

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.inventory.EquipmentHandler
import net.minestom.server.item.Enchantment
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt


fun Entity.applyKnockback(attacker: Entity, extra: Float = 1.0f) {
    val horizontalKnockback = 0.25 * 20.0 * extra

    val d0 = attacker.position.x() - position.x()
    val d1 = attacker.position.z() - position.z()

    val magnitude = sqrt(d0 * d0 + d1 * d1)

    val knockbackLevel = (attacker as? EquipmentHandler)?.itemInMainHand
        ?.meta?.enchantmentMap
        ?.get(Enchantment.KNOCKBACK) ?: 0
    var i = knockbackLevel.toDouble()

    if (attacker.isSprinting) i += 1.25

    var newVelocity = velocity
        .withX(((velocity.x() / 2) - (d0 / magnitude * horizontalKnockback)) )
        .withY((min((velocity.y() / 2) + 8, 8.0)))
        .withZ(((velocity.z() / 2) - (d1 / magnitude * horizontalKnockback)))

    if (i > 0) newVelocity = newVelocity.add(
        Vec(
            (-sin(attacker.position.yaw() * Math.PI / 180.0f) * i.toFloat() * horizontalKnockback),
            20.0,
            (cos(attacker.position.yaw() * Math.PI / 180.0f) * i.toFloat() * horizontalKnockback)
        )
    )

    if (newVelocity.y() > 8)
        newVelocity = newVelocity.withY(8.0)

    velocity = newVelocity

}

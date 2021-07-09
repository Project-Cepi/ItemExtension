package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.sound.SoundEvent
import net.minestom.server.tag.Tag


@Serializable
sealed class Attack(
    @Transient
    val displayName: String = "",
    @Transient
    val action: (Player, LivingEntity?) -> Boolean = { _, _ -> true },
    @Transient
    val requiresTarget: Boolean = false,
    @Transient
    val usedEnergy: Int = 0
) {

    @Serializable
    class Strike : Attack("Strike") {
        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is Strike) return false
            return true
        }
    }

    @Serializable
    class None: Attack("None", { _, _ -> false }) {
        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is None) return false
            return true
        }
    }

    @Serializable
    data class Yeet(val velocity: Double = 20.0) : Attack("Yeet", { _, target ->
        target!!.velocity.add(0.0, velocity, 0.0)
        true
    }, true)

    @Serializable
    data class Dash(val amount: Double = 15.0) : Attack("Dash", { player, _ ->
        player.velocity = player.position.direction.clone().normalize().multiply(amount)
        player.playSound(Sound.sound(SoundEvent.PLAYER_ATTACK_SWEEP, Sound.Source.MASTER, 1f, 1f))
        true
    }, usedEnergy = 5)

    @Serializable
    class Teleport : Attack("Teleport", { player, target ->
        player.teleport(target!!.position)
        false
    }, true) {
        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is Teleport) return false
            return true
        }
    }

    @Serializable
    class TargetAttack : Attack("Target Attack", { player, target ->
        target!!.damage(DamageType.fromPlayer(player), 1.0F)
        true
    }, true) {
        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is TargetAttack) return false
            return true
        }
    }

    companion object {
        inline fun <reified T: AttackTrait> generateTag(): Tag<String> = when (T::class) {
            PrimaryAttackTrait::class -> Tag.String("primary")
            SecondaryAttackTrait::class -> Tag.String("secondary")
            TertiaryAttackTrait::class -> Tag.String("tertiary")
            else -> throw IllegalAccessException("This should not happen!")
        }
    }

}
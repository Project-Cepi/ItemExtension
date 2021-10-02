package world.cepi.itemextension.item.traits.list.actions.list

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.sound.SoundEvent
import world.cepi.energy.energy
import world.cepi.itemextension.combat.TargetHandler
import world.cepi.itemextension.item.Item
import world.cepi.kstom.command.arguments.generation.annotations.DefaultNumber


@Serializable
sealed class Action {

    @Transient
    open val displayName: String = ""

    protected open operator fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean = true

    @Transient
    open val requiresTarget: Boolean = false

    @Transient
    open val usedEnergy: Int = 0

    @Serializable
    class Strike : Action() {

        override val displayName = "Strike"

        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is Strike) return false
            return true
        }
    }

    @Serializable
    data class Dash(
        @param:DefaultNumber(15.0)
        val amount: Double = 15.0
    ) : Action() {
        override val displayName = "Dash"
        override val usedEnergy = 5

        override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
            player.velocity = player.position.direction().normalize().mul(amount)
            player.playSound(Sound.sound(SoundEvent.ENTITY_PLAYER_ATTACK_SWEEP, Sound.Source.MASTER, 1f, 1f))

            return true
        }
    }

    @Serializable
    class Teleport : Action() {

        override val displayName = "Teleport"

        override val requiresTarget = true

        override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
            player.teleport(target!!.position)
            return false
        }

        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is Teleport) return false
            return true
        }
    }

    @Serializable
    class FlatDamage : Action() {

        override val requiresTarget = true

        override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
            target!!.damage(DamageType.fromPlayer(player), 1.0F)
            return true
        }

        override val displayName = "Flat Damage"

        override fun hashCode() = 0

        override fun equals(other: Any?): Boolean {
            if (other !is FlatDamage) return false
            return true
        }
    }

    operator fun invoke(player: Player, item: Item) {

        if (player.energy < this.usedEnergy) return

        if (TargetHandler[player] == null && this.requiresTarget) return

        player.energy -= this.usedEnergy

        invoke(player, TargetHandler[player], item)
    }

}
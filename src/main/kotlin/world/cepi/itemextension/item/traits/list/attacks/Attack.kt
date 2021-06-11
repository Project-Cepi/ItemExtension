package world.cepi.itemextension.item.traits.list.attacks

import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.sound.SoundEvent
import net.minestom.server.tag.Tag

enum class Attack(val displayName: String, val action: (Player, LivingEntity?) -> Boolean = { _, _ -> true }, val requiresTarget: Boolean = false) {

    STRIKE("Strike"),
    NONE("None", { _, _ -> false }),
    YEET("Yeet", { _, target ->
        target!!.velocity.add(0.0, 20.0, 0.0)
        true
    }, true),
    DASH("Dash", { player, _ ->
        player.velocity.add(player.position.direction.clone().normalize().multiply(6))
        player.playSound(Sound.sound(SoundEvent.PLAYER_ATTACK_SWEEP, Sound.Source.MASTER, 1f, 1f))
        true
    }),
    TELEPORT("Teleport", { player, target ->
        player.teleport(target!!.position)
        false
    }, true),
    TARGET_ATTACK("Target Attack", { player, target ->
        target!!.damage(DamageType.fromPlayer(player), 1.0F)
        true
    }, true);

    companion object {
        inline fun <reified T: AttackTrait> generateTag(): Tag<String> = when (T::class) {
            PrimaryAttackTrait::class -> Tag.String("primary")
            SecondaryAttackTrait::class -> Tag.String("secondary")
            TertiaryAttackTrait::class -> Tag.String("tertiary")
            else -> throw IllegalAccessException("This should not happen!")
        }
    }

}
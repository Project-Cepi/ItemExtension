package world.cepi.itemextension.item.traits.list.attacks

import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
import net.minestom.server.entity.damage.DamageType

enum class Attack(val displayName: String, val action: (Player, Player.Hand, LivingEntity) -> Boolean = { _, _, _ -> true }, val requiresTarget: Boolean = false) {

    STRIKE("Strike"),
    NONE("None", { _, _, _ -> false }),
    YEET("Yeet", { player, _, _ ->
        player.velocity.add(0.0, 20.0, 0.0)
        true
    }),
    DASH("Dash", { player, _, _ ->
        player.velocity.add(player.position.direction.clone().normalize().multiply(6))
        player.playSound(Sound.sound(SoundEvent.ENTITY_PLAYER_ATTACK_SWEEP, Sound.Source.MASTER, 1f, 1f))
        true
    }),
    TARGET_ATTACK("Target Attack", { player, _, target ->
        target.damage(DamageType.fromPlayer(player), 1.0F)
        true
    }, true)

}
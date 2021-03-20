package world.cepi.itemextension.item.traits.list.attacks

import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.Player

enum class Attack(val displayName: String, val action: (Player, Player.Hand) -> Boolean = { _, _ -> true } ) {

    STRIKE("Strike"),
    NONE("None", { _, _ -> false }),
    YEET("Yeet", { player, _ ->
        player.velocity.add(0.0, 20.0, 0.0)
        true
    }),
    DASH("Dash", { player, _ ->
        player.velocity.add(player.position.direction.clone().normalize().multiply(6))
        player.playSound(Sound.sound(net.minestom.server.sound.Sound.ENTITY_PLAYER_ATTACK_SWEEP, Sound.Source.MASTER, 1f, 1f))
        true
    })

}
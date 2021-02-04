package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.entity.Player
import net.minestom.server.sound.Sound
import net.minestom.server.sound.SoundCategory

enum class Attack(val displayName: String, val action: (Player, Player.Hand) -> Boolean = { _, _ -> true } ) {

    STRIKE("Strike"),
    NONE("None", { _, _ -> false }),
    DASH("Dash", { player, _ ->
        val direction = player.position.direction.normalize() // Get the general direction the player is looking at
        direction.multiply(6) // Boosts the direction
        player.velocity.add(direction)
        player.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1f, 1f)
        false
    })

}
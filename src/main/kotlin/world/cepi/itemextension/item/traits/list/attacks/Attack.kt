package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.entity.Player

enum class Attack(val displayName: String, val action: (Player, Player.Hand) -> Boolean = { _, _ -> true } ) {

    STRIKE("Strike"),
    NONE("None", { _, _ -> false }),
    YEET("Yeet", { player, _ ->
        player.velocity.add(0.0, 20.0, 0.0)
        true
    })

}
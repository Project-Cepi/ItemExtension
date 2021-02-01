package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.entity.Player

enum class Attack(val displayName: String, val action: (Player, Player.Hand) -> Unit = { _, _ -> }) {

    STRIKE("Strike"), // does nothing!
    DASH("Dash", { player, hand ->
        // TODO dash
    })

}
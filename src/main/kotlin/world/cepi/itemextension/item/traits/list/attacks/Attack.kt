package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.entity.Player

enum class Attack(val displayName: String, val action: (Player, Player.Hand) -> Unit = { _, _ -> }) {

    NONE("None"),
    COMPLIMENT("Compliment", { player, _ ->
        player.sendMessage("You're doing great!")
    })

}
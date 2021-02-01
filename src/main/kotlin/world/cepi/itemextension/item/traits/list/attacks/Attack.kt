package world.cepi.itemextension.item.traits.list.attacks

import net.minestom.server.entity.Player

enum class Attack(val displayName: String, val action: (Player) -> Unit = { }) {

    NONE("None"),
    COMPLIMENT("Compliment", {
        it.sendMessage("You're doing great!")
    })

}
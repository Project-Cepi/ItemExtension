package world.cepi.combat.events

import net.minestom.server.entity.Player

interface Handler {

    fun register(playerInit: Player)

}
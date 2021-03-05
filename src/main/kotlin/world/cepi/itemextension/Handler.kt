package world.cepi.itemextension

import net.minestom.server.entity.Player

internal interface Handler {

    fun register(playerInit: Player)

}
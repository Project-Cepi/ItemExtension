package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command

/** Loads when the command is registered */
interface ItemCommandLoader {

    fun register(command: Command)

}
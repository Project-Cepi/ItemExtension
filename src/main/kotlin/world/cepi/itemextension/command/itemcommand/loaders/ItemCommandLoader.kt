package world.cepi.itemextension.command.itemcommand.loaders

import net.minestom.server.command.builder.Command

/** Loads when the command is registered */
interface ItemCommandLoader {

    /** Loads and applies this function to the command located in ItemCommand. */
    fun register(command: Command)

}
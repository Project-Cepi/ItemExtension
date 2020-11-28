package world.cepi.itemextension.command.itemcommand.loaders

import net.minestom.server.command.builder.Command

/** Loads when the command is registered */
interface ItemCommandLoader {

    /**
     * Loads and applies this function to the command located in ItemCommand.
     *
     * @param command The command to apply syntaxes to.
     *
     */
    fun register(command: Command)

}
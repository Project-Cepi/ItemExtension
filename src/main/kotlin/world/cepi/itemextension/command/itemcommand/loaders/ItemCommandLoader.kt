package world.cepi.itemextension.command.itemcommand.loaders

import world.cepi.kstom.command.kommand.Kommand

/** Loads when the command is registered */
interface ItemCommandLoader {

    /**
     * Loads and applies this function to the command located in ItemCommand.
     *
     * @param command The command to apply syntaxes to.
     *
     */
    fun register(command: Kommand)

}
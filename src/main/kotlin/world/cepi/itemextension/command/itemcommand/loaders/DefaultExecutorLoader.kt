package world.cepi.itemextension.command.itemcommand.loaders

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.itemcommand.sendFormattedMessage
import world.cepi.itemextension.command.itemcommand.usage

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setDefaultExecutor { commandSender, _ ->
            commandSender.sendFormattedMessage("$usage: /item <create, reset, set, remove> <params>")
        }
    }

}
package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.sendFormattedMessage
import world.cepi.itemextension.command.usage

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setDefaultExecutor { commandSender, _ ->
            commandSender.sendFormattedMessage("$usage: /item <create, reset, set, remove> <params>")
        }
    }

}
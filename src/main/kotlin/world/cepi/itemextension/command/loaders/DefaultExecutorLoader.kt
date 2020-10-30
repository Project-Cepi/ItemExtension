package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.usage

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("$usage: /item <create, reset, set, remove> <params>")
        }
    }

}
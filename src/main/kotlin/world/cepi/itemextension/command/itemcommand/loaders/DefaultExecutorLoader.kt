package world.cepi.itemextension.command.itemcommand.loaders

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.itemcommand.usage
import world.cepi.kepi.messages.sendFormattedMessage

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setDefaultExecutor { commandSender, _ ->
            commandSender.sendFormattedMessage(Component.text("$usage: /item <create, reset, set, remove> <params>"))
        }
    }

}
package world.cepi.itemextension.command.itemcommand.loaders

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import world.cepi.kepi.messages.sendFormattedTranslatableMessage

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setDefaultExecutor { commandSender, _ ->
            commandSender.sendFormattedTranslatableMessage("common", "usage", Component.text("/item <create, reset, set, remove> <params>"))
        }
    }

}
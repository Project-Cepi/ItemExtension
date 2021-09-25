package world.cepi.itemextension.command.itemcommand.loaders

import net.kyori.adventure.text.Component
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object DefaultExecutorLoader : ItemCommandLoader {

    override fun register(command: Kommand) {
        command.default {
            sender.sendFormattedTranslatableMessage("common", "usage", Component.text("/item <create, reset, set, remove> <params>"))
        }
    }

}
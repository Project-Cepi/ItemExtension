package world.cepi.itemextension.command.itemcommand.loaders

import net.minestom.server.command.builder.Command
import world.cepi.kepi.messages.sendFormattedTranslatableMessage

object ConditionLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setCondition { sender, _ ->
            if (!sender.isPlayer) {
                sender.sendFormattedTranslatableMessage("common", "command.only_players")
                return@setCondition false
            } else return@setCondition true
        }
    }

}
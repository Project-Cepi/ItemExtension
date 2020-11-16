package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.onlyPlayers
import world.cepi.itemextension.command.sendFormattedMessage

object ConditionLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setCondition { sender, _ ->
            if (!sender.isPlayer) {
                sender.sendFormattedMessage(onlyPlayers)
                return@setCondition false
            } else return@setCondition true
        }
    }

}
package world.cepi.itemextension.command.loaders

import net.minestom.server.command.builder.Command
import world.cepi.itemextension.command.onlyPlayers

object ConditionLoader : ItemCommandLoader {

    override fun register(command: Command) {
        command.setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage(onlyPlayers)
                return@setCondition false
            } else return@setCondition true
        }
    }

}
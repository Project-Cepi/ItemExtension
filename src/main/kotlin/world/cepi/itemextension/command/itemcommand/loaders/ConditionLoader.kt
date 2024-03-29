package world.cepi.itemextension.command.itemcommand.loaders

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object ConditionLoader : ItemCommandLoader {

    override fun register(command: Kommand) {
        command.command.setCondition { sender, _ ->
            if (sender !is Player) {
                sender.sendFormattedTranslatableMessage("common", "command.only_players")
                return@setCondition false
            } else return@setCondition true
        }
    }

}
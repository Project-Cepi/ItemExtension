package world.cepi.itemextension.command

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType




class ItemCommand : Command("item") {

    init {
        val actionType = ArgumentType.Word("action").from("create", "reset", "set", "remove")

        addCallback({ commandSender: CommandSender, string: String, int: Int ->
            commandSender.sendMessage("\"$string\" is an invalid action! Valid actions include create, reset, set, and remove.")
        }, actionType)

        addSyntax({ commandSender: CommandSender, arguments: Arguments ->
            when (arguments.getWord("action")) {
                "reset" -> null
                else -> commandSender.sendMessage("\"${arguments.getWord("action")}\" requires paramaters!")
            }
        }, actionType);
    }

}
package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.MaterialTrait


class ItemCommand : Command("item") {

    init {
        val actionType = ArgumentType.Word("action").from("create", "set")

        val removeAction = ArgumentType.Word("action").from("remove")

        val resetAction = ArgumentType.Word("action").from("reset")

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("Usage: /item (create, reset, set, remove) <params>")
        }

        addCallback({ commandSender, string, _ ->
            commandSender.sendMessage("\"$string\" is an invalid action! Valid actions include create, reset, set, and remove.")
        }, resetAction)

        addCallback({ commandSender, string, _ ->
            commandSender.sendMessage("\"$string\" is an invalid action! Valid actions include create, reset, set, and remove.")
        }, removeAction)

        addCallback({ commandSender, string, _ ->
            commandSender.sendMessage("\"$string\" is an invalid action! Valid actions include create, reset, set, and remove.")
        }, actionType)

        setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage("The command is only available for players!")
                false
            } else true
        }

        addSyntax({ commandSender, _ ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand
            if (itemStack.data.hasKey(Item.key)) {
                val item = itemStack.data.get<Item>(Item.key)
                item.traits.removeIf { it !is MaterialTrait }
                player.itemInMainHand = item.renderItem()
                player.sendMessage("Item Reset!")
            } else {
                player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
            }
        }, resetAction);

        addSyntax({ commandSender, arguments ->
            commandSender.sendMessage("\"${arguments.getWord("action")}\" requires paramaters!")
        }, actionType);

        addSyntax({ commandSender, arguments ->
            commandSender.sendMessage("\"remove\" requires a trait to remove!")
        }, resetAction);
    }

}
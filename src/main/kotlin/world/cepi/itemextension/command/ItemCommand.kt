package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.data.DataImpl
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.MaterialTrait


class ItemCommand : Command("item") {

    init {

        val actions = ArgumentType.Word("action").from("create", "reset", "set", "remove")

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("Usage: /item (create, reset, set, remove) <params>")
        }


        setCondition { sender ->
            if (!sender.isPlayer) {
                sender.sendMessage("The command is only available for players!")
                false
            } else true
        }

        setArgumentCallback({ commandSender, _, _ ->
            commandSender.sendMessage("Invalid action! Actions include <reset, create, set, and remove>.")
        }, actions)

        addSyntax({ commandSender, args ->
            try {
                val player = commandSender as Player
                val itemStack = player.itemInMainHand

                if (itemStack.material == Material.AIR) {
                    player.sendMessage("You don't have an item in your hand! Please get one first!")
                    return@addSyntax
                }
                
                // data must be initialized for an itemStack
                if (itemStack.data == null) {
                    itemStack.data = DataImpl()
                }

                val isCepiItem = itemStack.data.get<Item>(Item.key) != null
                when (args.getWord("action")) {
                    "create" -> {
                        if (!isCepiItem) {
                            val item = Item()
                            item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                            player.itemInMainHand = item.renderItem(itemStack.amount)
                            player.sendMessage("Item created!")
                        }
                    }
                    "reset" -> {
                        if (isCepiItem) {
                            val item = itemStack.data.get<Item>(Item.key)
                            item.traits.removeIf { it !is MaterialTrait }
                            player.itemInMainHand = item.renderItem()
                            player.sendMessage("Item Reset!")
                        } else {
                            player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
                        }
                    }
                    "remove" -> {
                        if (isCepiItem) {
                            val item = itemStack.data.get<Item>(Item.key)
                            item.traits.removeIf { it !is MaterialTrait }
                            player.itemInMainHand = item.renderItem()
                            player.sendMessage("Trait Removed!")
                        } else {
                            player.sendMessage("You are not holding a formatted Item in your hand! Use /item create first.")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, actions)
    }

}
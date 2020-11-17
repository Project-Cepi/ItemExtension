package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.itemIsAir
import world.cepi.itemextension.command.itemcommand.itemRendered
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.requireFormattedItem
import world.cepi.itemextension.command.itemcommand.sendFormattedMessage
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem

object UpdateAction : ItemCommandLoader {
    override fun register(command: Command) {
        val update = ArgumentType.Word("update").from("update")

        command.addSyntax({ commandSender, _ ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.material == Material.AIR) {
                player.sendFormattedMessage(itemIsAir)
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)

            if (isCepiItem) {
                val item = itemStack.data!!.get<Item>(Item.key)!!
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedMessage(itemRendered)
            } else
                player.sendFormattedMessage(requireFormattedItem)
        }, update)

    }
}
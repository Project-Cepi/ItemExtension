package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemIsAir
import world.cepi.itemextension.command.itemReset
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.command.requireFormattedItem
import world.cepi.itemextension.command.sendFormattedMessage
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem

object ResetAction : ItemCommandLoader {
    override fun register(command: Command) {
        val reset = ArgumentType.Word("reset").from("reset")

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
                item.removeAllTraits()
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedMessage(itemReset)
            } else
                player.sendFormattedMessage(requireFormattedItem)
        }, reset)

    }
}
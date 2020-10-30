package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.checkIsItem
import world.cepi.itemextension.command.itemIsAir
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.command.requireFormattedItem
import world.cepi.itemextension.item.Item

object UpdateAction : ItemCommandLoader {
    override fun register(command: Command) {
        val update = ArgumentType.Word("update").from("update")

        command.addSyntax({ commandSender, _ ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand.clone()

            if (itemStack.material == Material.AIR) {
                player.sendMessage(itemIsAir)
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)

            if (isCepiItem) {
                val item = itemStack.data!!.get<Item>(Item.key)!!
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendMessage("Item Rendered!!")
            } else
                player.sendMessage(requireFormattedItem)
        }, update)

    }
}
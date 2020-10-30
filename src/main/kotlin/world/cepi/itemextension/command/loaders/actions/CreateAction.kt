package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.checkIsItem
import world.cepi.itemextension.command.itemIsAir
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.command.requireNonFormattedItem
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.MaterialTrait

object CreateAction : ItemCommandLoader {

    override fun register(command: Command) {

        val create = ArgumentType.Word("create").from("create")

        command.addSyntax({ commandSender, _ ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand.clone()

            if (itemStack.material == Material.AIR) {
                player.sendMessage(itemIsAir)
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)
            if (!isCepiItem) {
                val item = Item()
                item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendMessage("Item Created!")
            } else
                player.sendMessage(requireNonFormattedItem)
        }, create)
    }
}
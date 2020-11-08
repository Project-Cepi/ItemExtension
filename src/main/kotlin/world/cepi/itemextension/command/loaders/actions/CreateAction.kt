package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemCreated
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.command.requireNonFormattedItem
import world.cepi.itemextension.command.sendFormattedMessage
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.MaterialTrait

object CreateAction : ItemCommandLoader {

    override fun register(command: Command) {

        val create = ArgumentType.Word("create").from("create")

        command.addSyntax({ commandSender, _ ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            val isCepiItem = checkIsItem(itemStack)
            if (!isCepiItem) {
                val item = Item()

                if (itemStack.material != Material.AIR) {
                    item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                } else {
                    item.addTrait(MaterialTrait(Material.PAPER, itemStack.customModelData))
                }

                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedMessage(itemCreated)
            } else
                player.sendFormattedMessage(requireNonFormattedItem)
        }, create)
    }
}
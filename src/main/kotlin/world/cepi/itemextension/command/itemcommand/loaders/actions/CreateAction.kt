package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.itemCreated
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.requireNonFormattedItem
import world.cepi.itemextension.command.itemcommand.sendFormattedMessage
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.kstom.addSyntax
import world.cepi.kstom.arguments.asSubcommand

object CreateAction : ItemCommandLoader {

    override fun register(command: Command) {

        val create = "create".asSubcommand()

        command.addSyntax(create) { commandSender ->
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
        }
    }
}
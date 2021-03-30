package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.itemCreated
import world.cepi.itemextension.command.itemcommand.requireNonFormattedItem
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.addSyntax

object CreateAction : Command("create") {

    init {
        addSyntax { commandSender ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            val isCepiItem = checkIsItem(itemStack)
            if (!isCepiItem) {
                val item = Item()

                if (!itemStack.isAir) {
                    item.addTrait(MaterialTrait(itemStack.material, itemStack.customModelData))
                } else {
                    item.addTrait(MaterialTrait(Material.PAPER, itemStack.customModelData))
                }

                player.itemInMainHand = item.renderItem(if (itemStack.amount == 0.toByte()) 1 else itemStack.amount)
                player.sendFormattedMessage(Component.text(itemCreated))
            } else
                player.sendFormattedMessage(Component.text(requireNonFormattedItem))
        }
    }
}
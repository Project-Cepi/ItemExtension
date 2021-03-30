package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.itemIsAir
import world.cepi.itemextension.command.itemcommand.itemReset
import world.cepi.itemextension.command.itemcommand.requireFormattedItem
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.addSyntax

object ResetAction : Command("reset") {
    init {
        addSyntax { commandSender ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.material == Material.AIR) {
                player.sendFormattedMessage(Component.text(itemIsAir))
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)

            if (isCepiItem) {
                val item = itemStack.data!!.get<Item>(Item.key)!!
                item.removeAllTraits()
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedMessage(Component.text(itemReset))
            } else
                player.sendFormattedMessage(Component.text(requireFormattedItem))
        }

    }
}
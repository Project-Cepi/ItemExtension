package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax

object UpdateSubcommand : Command("update") {
    init {

        addSyntax { commandSender ->
            val player = commandSender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.material == Material.AIR) {
                player.sendFormattedTranslatableMessage("mob", "main.required")
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)

            if (isCepiItem) {
                val item = itemStack.data!!.get<Item>(Item.key)!!
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedTranslatableMessage("item", "render")
            } else
                player.sendFormattedTranslatableMessage("mob", "formatted.required")
        }

    }
}
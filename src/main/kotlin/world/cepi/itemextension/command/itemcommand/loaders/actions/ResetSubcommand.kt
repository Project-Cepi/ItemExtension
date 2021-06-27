package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.itemSerializationModule
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.item.get

object ResetSubcommand : Command("reset") {
    init {
        addSyntax {
            val player = sender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.material == Material.AIR) {
                player.sendFormattedTranslatableMessage("mob", "main.required")
                return@addSyntax
            }

            val isCepiItem = checkIsItem(itemStack)

            if (isCepiItem) {
                val item = itemStack.meta.get<Item>(Item.key, itemSerializationModule)!!
                item.removeAllTraits()
                player.itemInMainHand = item.renderItem(itemStack.amount)
                player.sendFormattedTranslatableMessage("item", "reset")
            } else
                player.sendFormattedTranslatableMessage("mob", "formatted.required")
        }

    }
}
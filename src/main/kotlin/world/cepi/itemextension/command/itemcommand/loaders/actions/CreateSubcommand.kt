package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax

object CreateSubcommand : Command("create") {

    init {
        addSyntax {
            val player = sender as Player
            val itemStack = player.itemInMainHand

            val isCepiItem = checkIsItem(itemStack)
            if (!isCepiItem) {
                val item = Item()

                if (!itemStack.isAir) {
                    item.put(MaterialTrait(itemStack.material, itemStack.meta.customModelData))
                }

                player.itemInMainHand = item.renderItem(if (itemStack.amount == 0) 1 else itemStack.amount)
                player.sendFormattedTranslatableMessage("item", "create")
            } else
                player.sendFormattedTranslatableMessage("item", "nonformatted.required")
        }
    }
}
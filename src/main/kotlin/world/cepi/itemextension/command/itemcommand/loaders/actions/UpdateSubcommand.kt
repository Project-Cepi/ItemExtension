package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.item.Material
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.checkIsItem
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object UpdateSubcommand : Kommand({

    syntax {
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendFormattedTranslatableMessage("item", "main.required")
            return@syntax
        }

        val isCepiItem = checkIsItem(itemStack)

        if (isCepiItem) {
            val item = itemStack.cepiItem!!
            player.itemInMainHand = item.renderItem(itemStack.amount)
            player.sendFormattedTranslatableMessage("item", "render")
        } else
            player.sendFormattedTranslatableMessage("item", "formatted.required")
    }

}, "update")
package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.minestom.server.item.Material
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.checkIsItem
import world.cepi.kepi.Kepi
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object ResetSubcommand : Kommand({

    syntax {
        val itemStack = player.itemInMainHand

        if (itemStack.material() == Material.AIR) {
            player.sendFormattedTranslatableMessage("mob", "main.required")
            return@syntax
        }

        val isCepiItem = checkIsItem(itemStack)

        if (isCepiItem) {
            val item = itemStack.cepiItem!!
            item.removeAllTraits()
            player.itemInMainHand = item.renderItem(itemStack.amount())
            player.sendFormattedTranslatableMessage("item", "reset")
            player.playSound(Kepi.editItemSound)
        } else
            player.sendFormattedTranslatableMessage("item", "formatted.required")
    }

}, "reset")
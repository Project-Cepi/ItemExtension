package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import world.cepi.gooey.InventoryManager
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand

object ClearCommand : Kommand({

    ConditionLoader.register(this)

    val playerArgument = ArgumentType.Entity("players").onlyPlayers(true)

    default {
        for (num in 0 until player.inventory.size) {

            // Don't clear permenant items.
            if (InventoryManager.slot(num) != null) continue

            player.inventory.setItemStack(num, ItemStack.AIR)
        }

        player.sendFormattedTranslatableMessage(
            "item",
            "clear.singular",
            Component.text(player.username, NamedTextColor.BLUE)
        )

    }

    syntax(playerArgument) {
        val entities = context.get(playerArgument).find(player)

        entities.forEach {
            (it as Player).inventory.clear()
        }

        player.sendFormattedTranslatableMessage(
            "item",
            "clear.${if (entities.size == 1) "plural" else "singular"}",
            if (entities.size == 1)
                Component.text((entities[0] as Player).username, NamedTextColor.BLUE)
            else
                Component.text(entities.size, NamedTextColor.BLUE)
        )
    }
}, "clear")
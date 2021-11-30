package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.kepi.command.subcommand.applyHelp
import world.cepi.kepi.item.AddCreationalItem
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kepi.messages.translations.formatTranslableMessage
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

object DataSubcommand : Kommand({

    val get by literal

    val from by literal
    val json = ArgumentType.StringArray("json").map {
        it.joinToString(" ")
    }

    syntax(get) {
        val itemStack = player.itemInMainHand

        if (itemStack.isAir) {
            player.sendFormattedTranslatableMessage("item", "main.required")
            return@syntax
        }

        val data = itemStack.cepiItem?.toJSON() ?: run {
            player.sendFormattedTranslatableMessage("item", "formatted.required")
            return@syntax
        }

        sender.sendMessage(
            Component.text(
                data,
                NamedTextColor.GRAY
            ).hoverEvent(
                HoverEvent.showText(
                    sender.formatTranslableMessage("common", "click.to_copy")
                        .color(NamedTextColor.GRAY)
                )
            ).clickEvent(
                ClickEvent.copyToClipboard(
                    data
                )
            )
        )

    }

    // TODO use translation
    applyHelp {
        """
            Data is the pure form of an item.
            
            You can get an item's data by using
            <yellow>/item data get
            
            And reapply it to an item by using
            <yellow>/item data from (json)
        """.trimIndent()
    }

    syntax(from, json) {
        val nbtData = context.get(json) // TODO process to json and convert

        try {
            val item = Item.fromJSON(nbtData)

            AddCreationalItem.put(player, item.renderItem())
        } catch (exception: Exception) {
            player.sendFormattedTranslatableMessage(
                "data",
                "malformed"
            )
        }
    }

}, "data")
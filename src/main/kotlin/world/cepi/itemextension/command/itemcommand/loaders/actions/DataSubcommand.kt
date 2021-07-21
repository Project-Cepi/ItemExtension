package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.kepi.command.subcommand.applyHelp
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kepi.messages.translations.formatTranslableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal

object DataSubcommand : Command("data") {

    init {
        val get = "get".literal()

        val from = "from".literal()
        val json = ArgumentType.StringArray("json").map {
            it.joinToString(" ")
        }

        addSyntax(get) {
            val player = sender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.isAir) {
                player.sendFormattedTranslatableMessage("item", "main.required")
                return@addSyntax
            }

            val data = itemStack.cepiItem?.toJSON() ?: run {
                player.sendFormattedTranslatableMessage("item", "formatted.required")
                return@addSyntax
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

        addSyntax(from, json) {
            val player = sender as Player

            val nbtData = context.get(json) // TODO process to json and convert

            try {
                val item = Item.fromJSON(nbtData)

                player.itemInMainHand = item.renderItem()
            } catch (exception: Exception) {
                player.sendFormattedTranslatableMessage(
                    "data",
                    "malformed"
                )
            }
        }
    }
}
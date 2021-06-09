package world.cepi.itemextension.command.itemcommand.loaders.actions

import kotlinx.serialization.Contextual
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.module
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kepi.messages.translations.formatTranslableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.item.get

object DataSubcommand : Command("data") {

    @Contextual
    val format = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        serializersModule = module
    }

    init {
        val get = "get".literal()

        val from = "from".literal()
        val json = ArgumentType.NbtCompound("json")

        addSyntax(get) { sender ->
            val player = sender as Player
            val itemStack = player.itemInMainHand

            if (itemStack.isAir) {
                player.sendFormattedTranslatableMessage("mob", "main.required")
                return@addSyntax
            }

            if (checkIsItem(itemStack)) {

                val data = format.encodeToString(itemStack.meta.get<Item>(Item.key, module))

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
        }

        addSyntax(from, json) { sender, args ->
            val player = sender as Player

            val nbtData = args.get(json) // TODO process to json and convert
        }
    }
}
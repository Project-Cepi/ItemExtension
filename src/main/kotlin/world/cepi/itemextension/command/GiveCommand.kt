package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand
import java.util.function.Supplier

object GiveCommand : Kommand({

    ConditionLoader.register(this)

    val itemArg = ArgumentType.ItemStack("item")
    val amountArg = ArgumentType.Integer("amount").min(1).max(127)

    val items = ArgumentType.Loop("loop", ArgumentType.Group("itemGroup", itemArg, amountArg))

    amountArg.defaultValue = Supplier { 1 }

    val selector = ArgumentType.Entity("players").onlyPlayers(true)

    syntax(selector, itemArg) {
        val targets = context.get(selector).find(player.instance!!, player).map { it as Player }

        targets.forEach { target ->
            (target as? Player)?.inventory?.addItemStack(!itemArg)
        }

        sender.sendFormattedTranslatableMessage(
            "item", "give",
            ((!itemArg).displayName!!),
            (
                    if (targets.size == 1) Component.text(targets[0].username)
                    else Component.text("${targets.size} Players")
                    ).color(NamedTextColor.BLUE)
        )
    }

    syntax(selector, items) {

        val targets = context.get(selector).find(player.instance!!, player).map { it as Player }

        targets.forEach { target ->
            (!items).forEach {
                val item = it.get(itemArg).withAmount(it.get(amountArg))
                (target as? Player)?.inventory?.addItemStack(item)
            }
        }

        val itemNames = (!items).map { context ->
            Component.text(context.get(amountArg), NamedTextColor.BLUE)
                .append(Component.space())
                .append(context.get(itemArg).displayName ?: Component.text(
                    context.get(itemArg).material()
                        .namespace().path()
                        .lowercase()
                        .split("_")
                        .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
                ) as Component).hoverEvent(context.get(itemArg).asHoverEvent())
        }.reduce { acc, component -> acc.append(Component.text(", ", NamedTextColor.GRAY)).append(component) }

        // TODO plural translation
        sender.sendFormattedTranslatableMessage(
            "item", "give",
            (itemNames),
            (
                    if (targets.size == 1) Component.text(targets[0].username)
                    else Component.text("${targets.size} Players")
            ).color(NamedTextColor.BLUE)
        )

    }

}, "give")
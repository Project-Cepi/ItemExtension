package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import java.util.function.Supplier

object GiveCommand : Command("give") {

    init {

        ConditionLoader.register(this)

        val itemArg = ArgumentType.ItemStack("item")
        val amountArg = ArgumentType.Integer("amount").min(1).max(127)

        amountArg.defaultValue = Supplier { 1 }

        val selector = ArgumentType.Entity("players").onlyPlayers(true)

        addSyntax(selector, itemArg, amountArg) {

            val player = sender as Player

            val item = context.get(itemArg).withAmount(context.get(amountArg))

            val targets = context.get(selector).find(player.instance!!, player).map { it as Player }

            targets.forEach {
                (it as? Player)?.inventory?.addItemStack(context.get(itemArg))
            }

            // TODO plural translation
            sender.sendFormattedTranslatableMessage(
                "item", "give",
                Component.text(context.get(amountArg), NamedTextColor.BLUE),
                (item.displayName
                    ?: Component.text(
                        item.material
                            .name
                            .lowercase()
                            .split("_")
                            .joinToString(" ") { it.capitalize() }
                    ))
                    .hoverEvent(item.asHoverEvent()),
                (
                        if (targets.size == 1) Component.text(targets[0].username)
                        else Component.text("${targets.size} Players")).color(NamedTextColor.BLUE)
            )

        }
    }

}
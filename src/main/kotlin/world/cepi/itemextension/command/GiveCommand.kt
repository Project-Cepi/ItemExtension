package world.cepi.itemextension.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.giveItem
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader
import world.cepi.kepi.messages.sendFormattedMessage

object GiveCommand : Command("give") {

    init {

        ConditionLoader.register(this)

        val itemArg = ArgumentType.ItemStack("item")
        val amountArg = ArgumentType.Integer("amount").min(1).max(127)

        amountArg.defaultValue = 1

        val selector = ArgumentType.Entity("players").onlyPlayers(true)

        addSyntax({ commandSender, args ->

            val player = commandSender as Player

            val item = args.get(itemArg)
            item.amount = args.get(amountArg).toByte()

            val targets = args.get(selector).find(player.instance!!, player).map { it as Player }

            targets.forEach {
                (it as? Player)?.inventory?.addItemStack(args.get(itemArg))
            }

            commandSender.sendFormattedMessage(
                giveItem,
                Component.text(args.get(amountArg), NamedTextColor.BLUE),
                (item.displayName
                    ?: Component.text(
                        item.material
                            .name
                            .toLowerCase()
                            .split("_")
                            .joinToString(" ") { it.capitalize() }
                    ))
                    .hoverEvent(item.asHoverEvent()),
                (if (targets.size == 1) Component.text(targets[0].username) else Component.text("${targets.size} Players")).color(NamedTextColor.BLUE)
            )

        }, selector, itemArg, amountArg)
    }

}
package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.itemextension.command.itemcommand.loaders.ConditionLoader

class GiveCommand : Command("give") {

    init {

        val itemArg = ArgumentType.ItemStack("item")
        val amountArg = ArgumentType.Integer("amount").min(1).max(127)
        val selector = ArgumentType.Entities("players").onlyPlayers(true)

        ConditionLoader.register(this)

        addSyntax({ commandSender, args ->

            val player = commandSender as Player

            args.get(selector).find(player.instance!!, null).forEach {
                (it as? Player)?.inventory?.addItemStack(args.get(itemArg))
            }

        }, selector, itemArg)

        addSyntax({ commandSender, args ->

            val player = commandSender as Player

            val item = args.get(itemArg)
            item.amount = args.get(amountArg).toByte()

            args.get(selector).find(player.instance!!, null).forEach {
                (it as? Player)?.inventory?.addItemStack(args.get(itemArg))
            }

        }, selector, itemArg, amountArg)
    }

}
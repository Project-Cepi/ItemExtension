package world.cepi.itemextension.command.loaders.actions

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.*
import world.cepi.itemextension.command.loaders.ItemCommandLoader
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.Traits

object RemoveAction : ItemCommandLoader {
    override fun register(command: Command) {

        val remove = ArgumentType.Word("remove").from("remove")

        val traitList = ArgumentType.Word("trait")
            .from(*Traits.values().map { it.name.toLowerCase() }
                .toTypedArray())

        command.addSyntax({ commandSender, args -> actionWithTrait(commandSender, args) }, remove, traitList)
    }

    private fun actionWithTrait(commandSender: CommandSender, args: Arguments) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendFormattedMessage(itemIsAir)
            return
        }

        val isCepiItem = checkIsItem(itemStack)

        if (isCepiItem) {
            val trait = Traits.values().first { it.name.equals(args.getWord("trait"), ignoreCase = true) }

            val item = itemStack.data!!.get<Item>(Item.key)!!

            if (item.hasTrait(trait.clazz)) {
                item.removeTrait(trait.clazz)
                player.sendFormattedMessage(traitRemoved)
                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
            } else
                player.sendFormattedMessage(traitNotFound)
        } else
            player.sendFormattedMessage(requireFormattedItem)
    }

}
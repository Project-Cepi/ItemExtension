package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.itemIsAir
import world.cepi.itemextension.command.itemcommand.loaders.ItemCommandLoader
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.command.itemcommand.requireFormattedItem
import world.cepi.itemextension.command.itemcommand.traitNotFound
import world.cepi.itemextension.command.itemcommand.traitRemoved
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kepi.messages.sendFormattedMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.asSubcommand

object RemoveAction : ItemCommandLoader {

    val traitList = ArgumentType.Word("trait")
        .from(*ItemTrait.classList.map { processTraitName(it.simpleName!!) }.toTypedArray())

    override fun register(command: Command) {

        val remove = "remove".asSubcommand()

        command.addSyntax(remove, traitList) { commandSender, args -> actionWithTrait(commandSender, args) }
    }

    private fun actionWithTrait(commandSender: CommandSender, context: CommandContext) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendFormattedMessage(Component.text(itemIsAir))
            return
        }

        val isCepiItem = checkIsItem(itemStack)

        if (isCepiItem) {
            val trait = ItemTrait.classList.first { processTraitName(it.simpleName!!).equals(context.get(traitList), ignoreCase = true) }

            val item = itemStack.data!!.get<Item>(Item.key)!!

            if (item.hasTrait(trait)) {
                item.removeTrait(trait)
                player.sendFormattedMessage(
                    Component.text(traitRemoved),
                    Component.text(processTraitName(trait.simpleName!!).substring(0..trait.simpleName!!.length - 5), NamedTextColor.BLUE)
                        .append(Component.text("", NamedTextColor.GRAY))
                )
                player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
            } else
                player.sendFormattedMessage(Component.text(traitNotFound))
        } else
            player.sendFormattedMessage(Component.text(requireFormattedItem))
    }

}
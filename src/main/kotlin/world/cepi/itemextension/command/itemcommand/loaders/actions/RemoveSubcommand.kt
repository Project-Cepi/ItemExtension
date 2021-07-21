package world.cepi.itemextension.command.itemcommand.loaders.actions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import world.cepi.itemextension.command.itemcommand.loaders.processTraitName
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kepi.messages.sendFormattedTranslatableMessage

object RemoveSubcommand : Command("remove") {

    val traitList = ArgumentType.Word("trait")
        .from(*ItemTrait.classList.map { processTraitName(it.simpleName!!) }.toTypedArray())

    init {
        addSyntax(::actionWithTrait, traitList)
    }

    private fun actionWithTrait(commandSender: CommandSender, context: CommandContext) {
        val player = commandSender as Player
        val itemStack = player.itemInMainHand

        if (itemStack.material == Material.AIR) {
            player.sendFormattedTranslatableMessage("item", "main.required")
            return
        }

        if (!checkIsItem(itemStack)) {
            player.sendFormattedTranslatableMessage("item", "formatted.required")
            return
        }

        val trait = ItemTrait.classList.first { processTraitName(it.simpleName!!).equals(context.get(traitList), ignoreCase = true) }

        val item = itemStack.cepiItem!!

        if (item.hasTrait(trait)) {
            item.removeTrait(trait)
            player.sendFormattedTranslatableMessage(
                "item", "trait.remove",
                Component.text(processTraitName(trait.simpleName!!), NamedTextColor.BLUE)
                    .append(Component.text("", NamedTextColor.GRAY))
            )
            player.itemInMainHand = item.renderItem(player.itemInMainHand.amount)
        } else
            player.sendFormattedTranslatableMessage("item", "trait.none", Component.text(context.get(traitList), NamedTextColor.BLUE))
    }

}
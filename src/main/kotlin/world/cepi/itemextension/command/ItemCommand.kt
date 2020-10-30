package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.command.loaders.loaders
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.Traits

class ItemCommand : Command("item") {

    init {

        setDefaultExecutor { commandSender, _ ->
            commandSender.sendMessage("$usage: /item <create, reset, set, remove> <params>")
        }

        val traitList = ArgumentType.Word("trait")
            .from(*Traits.values().map { it.name.toLowerCase() }
                .toTypedArray())

        setArgumentCallback({ commandSender, _, _ -> commandSender.sendMessage("Invalid trait!") }, traitList)

        loaders.forEach { it.register(this) }
    }

}

fun checkIsItem(itemStack: ItemStack): Boolean {
    // data must be initialized for an itemStack
    if (itemStack.data == null) itemStack.data = DataImpl()

    return itemStack.data!!.hasKey(Item.key)
}
package world.cepi.itemextension.command

import net.minestom.server.command.builder.Command
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.command.loaders.loaders
import world.cepi.itemextension.item.Item

class ItemCommand : Command("item") {

    init {
        loaders.forEach { it.register(this) }
    }

}

fun checkIsItem(itemStack: ItemStack): Boolean {
    // data must be initialized for an itemStack
    if (itemStack.data == null) itemStack.data = DataImpl()

    return itemStack.data!!.hasKey(Item.key)
}
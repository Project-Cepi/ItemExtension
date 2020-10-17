package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ColoredText
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.item.traits.Trait

class NameTrait(
    /** The name to be rendered on the item */
    private val name: String
) : Trait {

    override fun task(item: ItemStack) {
        item.displayName = ColoredText.of(name)
    }

}
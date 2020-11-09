package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.command.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.Trait
import world.cepi.itemextension.item.traits.getTrait

/** Represents the name that the item contains. */
class NameTrait(
    /** The name to be rendered on the item */
    private val name: String
) : Trait {

    override val taskIndex = 10

    override fun task(item: ItemStack) {
        val color = item.data?.get<Item>(Item.key)?.getTrait<Trait, RarityTrait>()?.rarity?.color ?: ChatColor.WHITE
        item.displayName = ColoredText.of(color + name)
    }

}
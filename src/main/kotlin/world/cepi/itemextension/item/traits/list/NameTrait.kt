package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

/** Represents the name that the item contains. */
@Serializable
@SerialName("name")
data class NameTrait(
    /** The name to be rendered on the item */
    val name: String
) : ItemTrait() {

    override val taskIndex = 10f

    override fun task(item: ItemStackBuilder, originalItem: Item) {
        val color = originalItem.get<RarityTrait>()?.rarity?.color ?: NamedTextColor.WHITE
        item.displayName(Component.text(name, color).decoration(TextDecoration.ITALIC, false))
    }

}
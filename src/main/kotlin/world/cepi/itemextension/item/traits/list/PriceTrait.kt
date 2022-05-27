package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemMeta
import net.minestom.server.tag.Tag
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

/**
 * Economy based trait. Used to define the price of an item.
 */
/** Represents the name that the item contains. */
@Serializable
@SerialName("price")
data class PriceTrait(
    /** The price of the item in economy units. */
    val price: Int
) : ItemTrait() {

    override val taskIndex = 0f

    override fun task(item: ItemMeta.Builder, originalItem: Item): Unit = with(item) {
        set(Tag.Integer("price"), price)
    }

}
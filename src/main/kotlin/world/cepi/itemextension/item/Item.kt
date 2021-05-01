package world.cepi.itemextension.item

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item.Companion.key
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.TraitContainer
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.get
import world.cepi.kstom.item.item
import world.cepi.kstom.item.withMeta

/** Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits. */
@Serializable
class Item: TraitContainer<ItemTrait>() {

    @Transient
    var requestedRenderMaterial: Material = Material.PAPER

    override val traits: MutableList<ItemTrait> = mutableListOf()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     *
     * @return The ItemStack after applying traits to the Item
     */
    fun renderItem(amount: Int = 1): ItemStack {

        val item = item(requestedRenderMaterial, amount) {

            withMeta {
                clientData {
                    this[key, module] = this@Item
                }
            }

            lore(traits.sortedBy { it.loreIndex }
                .map { trait -> trait.renderLore(this@Item) }
                .flatten())

            traits.sortedBy { it.taskIndex }
                .map { trait -> trait.task(this, this@Item) }
        }

        return item
    }


    companion object {
        /** Key for klaxon JSON storage. */
        const val key = "cepi-item"
    }


}

/**
 * Check if an [ItemStack] contains [Item] data.
 * It checks it via the itemstack's [DataImpl]
 *
 * @param itemStack The itemStack to check by
 *
 * @return If the [ItemStack] "is" an [Item]
 */
fun checkIsItem(itemStack: ItemStack): Boolean {
    // data must be initialized for an itemStack

    return itemStack.meta.get<Item>(key, module) != null
}
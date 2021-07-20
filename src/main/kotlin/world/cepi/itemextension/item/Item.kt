package world.cepi.itemextension.item

import kotlinx.serialization.Serializable
import net.minestom.server.data.DataImpl
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.itemextension.item.Item.Companion.key
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.ItemTraitMapSerializer
import world.cepi.itemextension.item.traits.TraitContainer
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.get
import world.cepi.kstom.item.item
import world.cepi.kstom.item.withMeta
import java.util.*
import kotlin.reflect.KClass

/** Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits. */
@Serializable
class Item: TraitContainer<ItemTrait>() {

    @Serializable(with = ItemTraitMapSerializer::class)
    override val traits = mutableMapOf<KClass<out ItemTrait>, ItemTrait>()

    /**
     * Renders an item to an ItemStack.
     *
     * @param amount The amount of the item to return
     *
     * @return The ItemStack after applying traits to the Item
     */
    fun renderItem(amount: Int = 1): ItemStack = item(
        get<MaterialTrait>()?.material ?: Material.PAPER,
        amount
    ) {

        withMeta {
            clientData {
                this[key, itemSerializationModule] = this@Item
            }

            hideFlag(*ItemHideFlag.values())
        }

        lore(traits.values.sortedBy { ItemTrait.classList.indexOf(it::class) }
            .map { trait -> trait.renderLore(this@Item) }
            .flatten())

        traits.values.sortedBy { it.taskIndex }
            .map { trait -> trait.task(this, this@Item) }
    }


    companion object {
        /** Key for kotlin JSON storage. */
        const val key = "cepi-item"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Item) return false

        return other.traits.values
            .sortedBy { it::class.simpleName!! }.toTypedArray()
            .contentEquals(this.traits.values.sortedBy { it::class.simpleName!! }.toTypedArray())
    }

    override fun toString(): String {
        return traits.values.fold("") { acc, trait ->
            return@fold "$acc,$trait"
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(*traits.values.toTypedArray())
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

    return itemStack.meta.get<Item>(key, itemSerializationModule) != null
}

/**
 * DSL for creating cepi items
 *
 * @param lambda The DSL receiver lambda.
 *
 * @return The item instance with applied changes
 */
fun cepiItem(lambda: Item.() -> Unit): Item = Item().also { it.lambda() }
package world.cepi.itemextension.item.traits

import com.beust.klaxon.Json
import net.minestom.server.item.ItemStack

/**
 * Trait objects that get appended to [Item]s. Inspired by the decorator pattern
 */
interface Trait : Cloneable {

    /**
     * The position where this trait is rendered in the item lore (for item rendering).
     */
    @Json(ignored = true)
    val loreIndex: Int
        get() = -1

    /**
     * This function runs at item render time.
     */
    fun task(item: ItemStack) {

    }

    /**
     * This function is called based on the [loreIndex] of the [Trait]
     */
    fun renderLore(): List<String> {
        return arrayListOf()
    }

    public override fun clone(): Any {
        return super.clone()
    }

}
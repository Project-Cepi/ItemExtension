package world.cepi.itemextension.item.traits

import com.beust.klaxon.Json
import net.minestom.server.chat.ColoredText
import net.minestom.server.item.ItemStack

/** Trait objects that get appended to Items. Inspired by the decorator pattern */
interface Trait {

    /** The position where this trait is rendered in the item lore (for item rendering). */
    @Json(ignored = true)
    val loreIndex: Int
        get() = -1

    /**
     * This function runs at item render time.
     *
     * @param item The item that is being rendered.
     */
    fun task(item: ItemStack) { }

    /**
     * This function is called based on the [loreIndex] of the [Trait]
     *
     * @return A list of ColoredText used to display on an array
     */
    fun renderLore(): List<ColoredText> {
        return arrayListOf()
    }

}
package world.cepi.itemextension.item.traits

import net.minestom.server.item.ItemStack

/** Trait objects that get appended to Items. Inspired by the decorator pattern */
interface Trait {

    /** The position where this trait is rendered in the item lore (for item rendering). */
    val loreIndex: Int
        get() = -1

    /** The position where this task is run in the item (for item rendering). */
    val taskIndex: Int
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
    fun renderLore(): List<String> {
        return arrayListOf()
    }

}
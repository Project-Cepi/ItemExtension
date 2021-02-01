package world.cepi.itemextension.item.traits

import world.cepi.itemextension.item.KItem

/** Trait objects that get appended to Items. Inspired by the decorator pattern */
interface Trait {

    /** The position where this trait is rendered in the item lore (for item rendering). */
    val loreIndex: Int

    /** The position where this task is run in the item (for item rendering). */
    val taskIndex: Int

    /**
     * This function runs at item render time.
     *
     * @param item The item that is being rendered.
     */
    fun task(item: KItem) { }

    /**
     * This function is called based on the [loreIndex] of the [Trait]
     *
     * @return A list of ColoredText used to display on an array
     */
    fun renderLore(): List<String> {
        return arrayListOf()
    }

}
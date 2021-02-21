package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.item.Item

class TypeTrait(
        /** The type, used to hold the value in TypeTrait. */
        val type: Type
) : ItemTrait() {

    override val loreIndex = 0f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return arrayListOf("${ChatColor.GRAY}${type.name.toLowerCase().capitalize()}", "")
    }

    /** Type enum for handling item types */
    enum class Type {
        ARMOR,
        MELEE
    }

}
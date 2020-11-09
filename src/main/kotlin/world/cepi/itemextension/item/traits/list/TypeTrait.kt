package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.item.traits.Trait

class TypeTrait(
        /** The type, used to hold the value in TypeTrait. */
        private val type: Type
) : Trait {

    override val loreIndex = 0

    override fun renderLore(): List<String> {
        return arrayListOf("${ChatColor.GRAY}${type.name}", "")
    }

    /** Type enum for handling item types */
    enum class Type {
        WEAPON,
        ARMOR
    }

}
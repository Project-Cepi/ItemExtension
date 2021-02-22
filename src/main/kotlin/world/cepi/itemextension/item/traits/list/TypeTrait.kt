package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

@Serializable
@SerialName("itemtype")
class TypeTrait(
        /** The type, used to hold the value in TypeTrait. */
        @SerialName("itemtype") // Renamed for JSON specifications
        val type: Type
) : ItemTrait() {

    override val loreIndex = 0f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return if (item.softHasTrait<LevelTrait>()) {
            arrayListOf("${ChatColor.GRAY}${type.name.toLowerCase().capitalize()}")
        } else {
            arrayListOf("${ChatColor.GRAY}${type.name.toLowerCase().capitalize()}", "")
        }
    }

    /** Type enum for handling item types */
    enum class Type {
        ARMOR,
        MELEE
    }

}
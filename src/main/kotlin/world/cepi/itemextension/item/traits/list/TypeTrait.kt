package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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

    override fun renderLore(item: Item): List<Component> {
        return if (item.softHasTrait<LevelTrait>()) {
            arrayListOf(Component.text(type.name.toLowerCase().capitalize(), NamedTextColor.GRAY))
        } else {
            arrayListOf(Component.text(type.name.toLowerCase().capitalize(), NamedTextColor.GRAY), Component.space())
        }
    }

    /** Type enum for handling item types */
    enum class Type {
        ARMOR,
        MELEE
    }

}
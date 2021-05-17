package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

@Serializable
@SerialName("itemtype")
class TypeTrait(
        /** The type, used to hold the value in TypeTrait. */
        @SerialName("itemtype") // Renamed as "type" is a reserved keyword in kotlinx.serialization
        val type: Type
) : ItemTrait() {

    override val loreIndex = 0f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return arrayListOf(
            Component.text(
                type.name
                    .lowercase()
                    .replaceFirstChar { it.titlecase() }, // Capitalize first char of word.
                NamedTextColor.GRAY
            ).decoration(TextDecoration.ITALIC, false)
        ).also {
            if (item.softHasTrait<LevelTrait>()) it.add(Component.space())
        }
    }

    /** Type enum for handling item types */
    enum class Type {
        ARMOR,
        MELEE
    }

}
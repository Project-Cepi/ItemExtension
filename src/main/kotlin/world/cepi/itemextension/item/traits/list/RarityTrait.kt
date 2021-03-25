package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

/** Defines the overall chance that this item would even exist */
@Serializable
@SerialName("rarity")
class RarityTrait(
    /** The rarity that the [RarityTrait] encapsulates. */
    val rarity: Rarity
) : ItemTrait() {

    override val loreIndex = 10f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.space(),
            rarity.asComponent()
                .decoration(TextDecoration.ITALIC, false)
        )
    }

    /** Rarity enum for handling item rarities. */
    enum class Rarity (
        /** Number that identifies the Rarity for future-proofing items. 1 is the lowest, goes up to highest */
        val number: Int,

        /** The color of the rarity */
        val color: NamedTextColor
    ) {

        /** Nothing special about this item, found in shops or dropped*/
        BASIC(0, NamedTextColor.GRAY),
        /** Item found in hidden places such as loot chests */
        ARTIFACT(1, NamedTextColor.LIGHT_PURPLE),
        /** Item received as a reward for completing a challenge */
        TREASURE(2, NamedTextColor.YELLOW),
        /** Item that embodies a part of you */
        VESSEL(3, NamedTextColor.BLUE),
        /** Given after doing something for someone. */
        TRIBUTE(4, NamedTextColor.GREEN);

        fun asComponent(): Component {
            return Component.text(this.name).style(Style.style(this.color, TextDecoration.BOLD))
        }

    }

}
package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import world.cepi.itemextension.item.traits.Trait

class RarityTrait(
    /**
     * The rarity that the [RarityTrait] encapsulates.
     */
    val rarity: Rarity
) : Trait {

    override val loreIndex = 3

    override fun renderLore(): List<ColoredText> {
        return listOf(ColoredText.of(""), rarity.asString())
    }

    /**
     * Rarity enum for handling item rarities.
     */
    enum class Rarity(
        /**
         * Number that identifies the Rarity for future-proofing items. 1 is the lowest, goes up to highest
         */
        val number: Int,

        /**
         * The color of the rarity
         */
        val color: ChatColor
    ) {

        COMMON(1, ChatColor.GRAY),
        UNCOMMON(2, ChatColor.BRIGHT_GREEN),
        RARE(3, ChatColor.BLUE),
        EPIC(4, ChatColor.PINK),
        LEGENDARY(5, ChatColor.RED),
        MYTHICAL(6, ChatColor.CYAN);

        fun asString(): ColoredText {
            return ColoredText.of(ChatColor.BOLD, "").append(this.color, this.name)
        }

    }

}
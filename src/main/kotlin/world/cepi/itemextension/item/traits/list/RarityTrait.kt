package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.plus
import world.cepi.itemextension.item.traits.Trait

/** Defines the overall chance that this item would even exist */
class RarityTrait(
    /** The rarity that the [RarityTrait] encapsulates. */
    private val rarity: Rarity
) : Trait {

    override val loreIndex = 3

    override fun renderLore(): List<String> {
        return listOf(rarity.asString())
    }

    /** Rarity enum for handling item rarities. */
    enum class Rarity (
        /** Number that identifies the Rarity for future-proofing items. 1 is the lowest, goes up to highest */
        val number: Int,

        /** The color of the rarity */
        private val color: ChatColor
    ) {

        COMMON(1, ChatColor.GRAY),
        UNCOMMON(2, ChatColor.BRIGHT_GREEN),
        RARE(3, ChatColor.BLUE),
        EPIC(4, ChatColor.PINK),
        LEGENDARY(5, ChatColor.RED),
        MYTHICAL(6, ChatColor.CYAN);

        fun asString(): String {
            return ChatColor.BOLD + this.color + this.name
        }

    }

}
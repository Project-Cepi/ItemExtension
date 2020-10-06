package world.cepi.itemextension.item

import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import java.awt.Color

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

    fun asString(rarity: Rarity): ColoredText {
        return ColoredText.of(rarity.color, rarity.name)
    }

}
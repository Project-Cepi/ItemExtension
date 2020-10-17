package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import world.cepi.itemextension.item.traits.Trait

class LevelTrait(
        val level: Int
) : Trait {

    override val loreIndex = 0

    override fun renderLore(): List<ColoredText> {
        return listOf(ColoredText.of(ChatColor.GRAY, "Level Min. ").append(ChatColor.WHITE, level.toString()))
    }

}
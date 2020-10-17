package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import world.cepi.itemextension.item.traits.Trait

class DamageTrait(
    val damage: Int
) : Trait {

    override val loreIndex = 1

    override fun renderLore(): List<ColoredText> {
        return listOf(ColoredText.of(ChatColor.YELLOW, "$damage Attack Damage"))
    }

}
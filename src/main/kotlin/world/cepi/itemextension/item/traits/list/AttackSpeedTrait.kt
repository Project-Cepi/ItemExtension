package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus

class AttackSpeedTrait(
        /** The attack speed of the trait. */
        val attackSpeed: Double
) : ItemTrait() {

    override val loreIndex = 4

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed")
    }

}
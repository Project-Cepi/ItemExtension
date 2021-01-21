package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus

class AttackSpeedTrait(
        /** The attack speed of the trait. */
        val attackSpeed: Double
) : ItemTrait() {

    override val loreIndex = 3
    override val taskIndex = 1

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed")
    }

}
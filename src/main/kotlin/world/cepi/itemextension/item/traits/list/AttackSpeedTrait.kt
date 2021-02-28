package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.command.itemcommand.plus
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.list.stats.StatTrait

@Serializable
@SerialName("attack_speed")
class AttackSpeedTrait(
        /** The attack speed of the trait. */
        val attackSpeed: Double
) : ItemTrait() {

    override val loreIndex = 5f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return if (item.softHasTrait<StatTrait>()) {
            listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed", "")
        } else {
            listOf(ChatColor.GRAY + attackSpeed.toString() + "s Attack Speed")
        }
    }

}
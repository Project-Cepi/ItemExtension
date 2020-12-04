package world.cepi.itemextension.item.traits.list

import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.attribute.Attributes
import net.minestom.server.chat.ChatColor
import net.minestom.server.item.ItemStack
import net.minestom.server.item.attribute.AttributeSlot
import net.minestom.server.item.attribute.ItemAttribute
import world.cepi.itemextension.command.itemcommand.plus
import java.util.*

/** The damage this brings upon attackers */
class DamageTrait(
    val damage: Double
) : ItemTrait() {

    override val loreIndex = 1

    override fun renderLore(): List<String> {
        return listOf(ChatColor.GOLD + "+$damage Damage")
    }

    override fun task(item: ItemStack) {
        applyAttribute(item, AttributeSlot.MAINHAND)
        applyAttribute(item, AttributeSlot.OFFHAND)
    }

    private fun applyAttribute(item: ItemStack, slot: AttributeSlot) {
        item.addAttribute(ItemAttribute(UUID.randomUUID(), item.displayName.toString(), Attributes.ATTACK_DAMAGE, AttributeOperation.ADDITION,
            damage, slot))
    }


}
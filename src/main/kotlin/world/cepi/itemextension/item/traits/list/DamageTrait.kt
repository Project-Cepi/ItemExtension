package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.KItem
import world.cepi.itemextension.item.traits.ItemTrait

/** The damage this brings upon attackers */

@Serializable
@SerialName("damage")
class DamageTrait(
    val damage: Double
) : ItemTrait() {

    override val loreIndex = 3f
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return listOf(Component.space(), Component.text("+$damage Damage", NamedTextColor.GOLD))
    }

    override fun task(item: KItem) {
        item.data!!.set("damage", damage)
    }

}
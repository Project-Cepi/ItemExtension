package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

// TODO make armor trait functional
@SerialName("armor")
@Serializable
data class ArmorTrait(
    val armor: Float
) : ItemTrait() {

    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text("+$armor Armor", NamedTextColor.AQUA)
                .decoration(TextDecoration.ITALIC, false)
        )
    }

    companion object {
        fun applyToDamage(armor: Float, damage: Float): Float =
            (damage - armor).coerceAtLeast(0.0f)
    }


}
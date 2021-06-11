package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.adventure.asMini

@Serializable
@SerialName("custom-text")
class CustomTextTrait(
    val text: String,
) : ItemTrait() {
    override val taskIndex: Float = 1F

    override fun renderLore(item: Item): List<Component> {
        return listOf(text.asMini())
    }
}
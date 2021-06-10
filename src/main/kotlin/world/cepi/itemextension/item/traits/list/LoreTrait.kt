package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import java.util.*
import java.util.regex.Pattern

@Serializable
@SerialName("lore")
data class LoreTrait(
        val lore: String
) : ItemTrait() {

    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.space(),
            *splitString(lore, 30).map {
                Component.text(it).style(Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC))
            }.toTypedArray())
    }

    private fun splitString(msg: String, lineSize: Int): List<String> {
        val res: MutableList<String> = ArrayList()
        val p = Pattern.compile("\\b.{1," + (lineSize - 1) + "}\\b\\W?")
        val m = p.matcher(msg)
        while (m.find()) {
            res.add(m.group())
        }
        return res
    }

}
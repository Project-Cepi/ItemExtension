package world.cepi.itemextension.item.traits.list

import net.minestom.server.chat.ChatColor
import world.cepi.itemextension.item.Item
import java.util.regex.Pattern

class LoreTrait(
        val lore: String
) : ItemTrait() {

    override val loreIndex = 20f // bottom of the barrel
    override val taskIndex = 1f

    override fun renderLore(item: Item): List<String> {
        return listOf("", *splitString(lore, 30).toTypedArray())
    }

    private fun splitString(msg: String, lineSize: Int): List<String> {
        val res: MutableList<String> = ArrayList()
        val p = Pattern.compile("\\b.{1," + (lineSize - 1) + "}\\b\\W?")
        val m = p.matcher(msg)
        while (m.find()) {
            res.add("" + ChatColor.GRAY + ChatColor.ITALIC + m.group())
        }
        return res
    }

}
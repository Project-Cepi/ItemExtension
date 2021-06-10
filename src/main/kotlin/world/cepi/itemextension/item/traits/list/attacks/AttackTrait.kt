package world.cepi.itemextension.item.traits.list.attacks

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait

sealed class AttackTrait: ItemTrait() {

    open val attack: Attack = Attack.STRIKE
    open val clickType: String = "None"

    override val taskIndex = 0f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text(attack.displayName, NamedTextColor.RED)
                .append(Component.text(" [${clickType}]", NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false)
        )
    }

}
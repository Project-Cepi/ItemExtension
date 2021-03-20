package world.cepi.itemextension.item.traits.list.stats

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.TraitRefrenceList

abstract class StatTrait : ItemTrait() {

    companion object: TraitRefrenceList(
            HealthStatTrait::class,
            SpeedStatTrait::class
    )

    override val loreIndex = 6f
    override val taskIndex = 1f

    open val name: String = "Example"
    open val value: Float = 0.0F

    override fun renderLore(item: Item): List<Component> {
        return if (value >= 0)
            listOf(
                Component.text("► ", NamedTextColor.DARK_GRAY)
                    .append(Component.text("+$value ", NamedTextColor.GREEN))
                    .append(Component.text(name, NamedTextColor.GRAY)))
        else
            listOf(
                Component.text("► ", NamedTextColor.DARK_GRAY)
                    .append(Component.text("-$value ", NamedTextColor.RED))
                    .append(Component.text(name, NamedTextColor.GRAY)))
    }

}
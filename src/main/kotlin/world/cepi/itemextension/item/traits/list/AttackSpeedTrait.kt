package world.cepi.itemextension.item.traits.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.itemextension.item.traits.list.stats.StatTrait
import world.cepi.kstom.serializer.DurationSerializer
import java.time.Duration

@Serializable
@SerialName("attack_speed")
data class AttackSpeedTrait(
        /** The action speed of the trait. */
        @Serializable(with = DurationSerializer::class)
        val attackSpeed: Duration
) : ItemTrait() {

    override val taskIndex = 1f

    override fun renderLore(item: Item): List<Component> {
        return arrayListOf(
            Component.text("${attackSpeed.toMillis() / 1000}s Action Speed", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
        ).also {
            if (item.softHasTrait<StatTrait>()) {
                it.add(Component.space())
            }
        }
    }

}
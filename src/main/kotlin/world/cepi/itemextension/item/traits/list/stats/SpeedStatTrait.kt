package world.cepi.itemextension.item.traits.list.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("speed_stat")
data class SpeedStatTrait(override val value: Float): StatTrait() {

    override val name = "Speed"

}
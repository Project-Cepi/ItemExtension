package world.cepi.itemextension.item.traits.list.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("speed_stat")
class SpeedStatTrait(override val value: Double): StatTrait() {

    override val name = "Speed"

}
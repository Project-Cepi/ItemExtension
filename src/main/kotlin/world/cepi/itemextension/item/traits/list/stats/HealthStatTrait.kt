package world.cepi.itemextension.item.traits.list.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("health_stat")
data class HealthStatTrait(override val value: Float): StatTrait() {

    override val name = "Health"

}
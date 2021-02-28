package world.cepi.itemextension.item.traits.list.stats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("health_stat")
class HealthStatTrait(override val value: Double): StatTrait() {

    override val name = "Health"


}
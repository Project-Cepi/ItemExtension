package world.cepi.itemextension.item.traits.list.attributes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("health_attribute")
class HealthAttributeTrait(override val value: Double): AttributeTrait() {

    override val name = "Health"


}
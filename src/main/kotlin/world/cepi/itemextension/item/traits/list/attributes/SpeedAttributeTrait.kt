package world.cepi.itemextension.item.traits.list.attributes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("speed_attribute")
class SpeedAttributeTrait(override val value: Double): AttributeTrait() {

    override val name = "Speed"

}
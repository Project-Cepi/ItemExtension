package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tertiary_action")
data class TertiaryActionTrait(override val action: Action): ActionTrait() {
    override val clickType = "Right"

}
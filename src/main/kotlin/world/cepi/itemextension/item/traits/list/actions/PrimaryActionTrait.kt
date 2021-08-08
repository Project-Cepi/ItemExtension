package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("primary_action")
data class PrimaryActionTrait(override val action: Action): ActionTrait() {
    override val clickType = "Left"
}
package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.traits.list.actions.list.Action

@Serializable
@SerialName("secondary_action")
data class SecondaryActionTrait(override val action: Action): ActionTrait() {
    override val clickType = "Sneak + Left"

}
package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.actions.Action
import world.cepi.actions.context.ActionItemOffContextParser
import world.cepi.kstom.command.arguments.generation.annotations.ParameterContext

@Serializable
@SerialName("secondary_action")
data class SecondaryActionTrait(
    @ParameterContext(ActionItemOffContextParser::class)
    override val action: Action,
    override val displayName: String,
    override val useTargeting: Boolean
): ActionTrait() {
    override val clickType = "Sneak + Left"

}
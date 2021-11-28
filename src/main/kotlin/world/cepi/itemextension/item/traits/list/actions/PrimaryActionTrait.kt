package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.actions.ActionItem
import world.cepi.actions.context.ActionItemOffContextParser
import world.cepi.kstom.command.arguments.generation.annotations.ParameterContext

@Serializable
@SerialName("primary_action")
class PrimaryActionTrait(
    override val displayName: String,
    override val useTargeting: Boolean,
    @ParameterContext(ActionItemOffContextParser::class)
    override val action: ActionItem
): ActionTrait() {
    override val clickType = "Left"
}
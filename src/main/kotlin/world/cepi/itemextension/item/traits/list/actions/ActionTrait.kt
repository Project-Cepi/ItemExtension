package world.cepi.itemextension.item.traits.list.actions

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.*
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.item.ItemStack
import world.cepi.actions.Action
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.raycast.HitType
import world.cepi.kstom.raycast.RayCast

@Serializable
sealed class ActionTrait: ItemTrait() {

    abstract val action: Action
    abstract val displayName: String
    abstract val useTargeting: Boolean

    abstract val clickType: String

    override val taskIndex = 0f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text(displayName, NamedTextColor.RED)
                .append(Component.text(" [${clickType}]", NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false)
        )
    }

    companion object {

        val itemNode by lazy { EventNode.type("item-handler-actions", EventFilter.PLAYER) }

        fun <T> rightClick(event: T): Unit where T : PlayerEvent = with(event) {

            if (player.itemInMainHand == ItemStack.AIR) return@with

            val itemStack = player.itemInMainHand

            val cepiItem = itemStack.cepiItem ?: return@with

            if (!cepiItem.hasTrait<TertiaryActionTrait>())
                return

            val action = cepiItem.get<TertiaryActionTrait>()

            action!!.action(player, player)
        }

        fun <T> leftClick(event: T): Unit where T : PlayerEvent = with(event) {

            if (player.itemInMainHand == ItemStack.AIR) return@with

            val itemStack = player.itemInMainHand

            val cepiItem = itemStack.cepiItem ?: return@with

            val actionTrait = if (player.isSneaking && cepiItem.hasTrait<SecondaryActionTrait>()) {
                cepiItem.get<SecondaryActionTrait>()!!
            } else if (cepiItem.hasTrait<PrimaryActionTrait>()) {
                cepiItem.get<PrimaryActionTrait>()!!
            } else {
                return@with
            }

            actionTrait.action(player, player)
        }

        init {
            itemNode.listenOnly<PlayerUseItemEvent>(::rightClick)
            itemNode.listenOnly<PlayerUseItemOnBlockEvent>(::rightClick)
            itemNode.listenOnly<PlayerEntityInteractEvent>(::rightClick)
            itemNode.listenOnly<PlayerStartDiggingEvent>(::leftClick)
            itemNode.listenOnly<PlayerHandAnimationEvent> {
                if (hand == Player.Hand.MAIN && RayCast.castRay(
                        player.instance!!,
                        player,
                        player.position.asVec().add(.0, player.eyeHeight, .0),
                        player.position.direction(),
                        5.0
                    ).hitType == HitType.NONE)
                    leftClick(this)
            }
        }
    }

}
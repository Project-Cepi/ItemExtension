package world.cepi.itemextension.item.traits.list.attacks

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.*
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.item.ItemStack
import world.cepi.energy.energy
import world.cepi.itemextension.combat.TargetHandler
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.itemSerializationModule
import world.cepi.itemextension.item.traits.ItemTrait
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.item.get
import world.cepi.kstom.raycast.HitType;
import world.cepi.kstom.raycast.RayCast;

sealed class AttackTrait: ItemTrait() {

    open val attack: Attack = Attack.Strike()
    open val clickType: String = "None"

    override val taskIndex = 0f

    override fun renderLore(item: Item): List<Component> {
        return listOf(
            Component.text(attack.displayName, NamedTextColor.RED)
                .append(Component.text(" [${clickType}]", NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false)
        )
    }

    companion object {

        private fun useAttack(player: Player, attack: Attack) {

            if (player.energy < attack.usedEnergy) return

            if (TargetHandler[player] == null && attack.requiresTarget) return

            attack.action(player, TargetHandler[player])

            player.energy -= attack.usedEnergy
        }

        val itemNode = EventNode.type("item-handler-attacks", EventFilter.PLAYER)

        fun <T> rightClick(event: T): Unit where T : PlayerEvent = with(event) {

            if (player.itemInMainHand == ItemStack.AIR) return@with

            val itemStack = player.itemInMainHand

            if (!itemStack.hasTag(Attack.generateTag<TertiaryAttackTrait>()))
                return

            val cepiItem = itemStack.meta.get<Item>(Item.key, itemSerializationModule)

            val attack = cepiItem!!.get<SecondaryAttackTrait>()

            useAttack(player, attack!!.attack)
        }

        fun <T> leftClick(event: T): Unit where T : PlayerEvent = with(event) {

            if (player.itemInMainHand == ItemStack.AIR) return@with

            val itemStack = player.itemInMainHand

            if (!checkIsItem(itemStack)) return@with

            val cepiItem = itemStack.meta.get<Item>(Item.key, itemSerializationModule)!!

            if (
                player.isSneaking && itemStack.hasTag(Attack.generateTag<SecondaryAttackTrait>())
            ) {
                val attack = cepiItem.get<SecondaryAttackTrait>()

                useAttack(player, attack!!.attack)
            } else if (
                itemStack.hasTag(Attack.generateTag<PrimaryAttackTrait>())
            ) {
                val attack = cepiItem.get<SecondaryAttackTrait>()

                useAttack(player, attack!!.attack)
            }
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
                        player.position.toVector().clone().add(.0, player.eyeHeight, .0),
                        player.position.direction,
                        5.0
                    ).hitType == HitType.NONE)
                    leftClick(this)
            }
        }
    }

}
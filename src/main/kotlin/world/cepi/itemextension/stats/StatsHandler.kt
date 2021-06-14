package world.cepi.itemextension.stats

import net.minestom.server.attribute.Attribute
import net.minestom.server.attribute.AttributeModifier
import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.entity.LivingEntity
import net.minestom.server.event.Event
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.item.EntityEquipEvent
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.module
import world.cepi.itemextension.item.traits.list.stats.HealthStatTrait
import world.cepi.itemextension.item.traits.list.stats.SpeedStatTrait
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.item.get
import java.util.*

object StatsHandler {

    const val healthStat = "health_item"
    val healthUUID: UUID = UUID.randomUUID()

    const val speedStat = "speed_item"
    val speedUUID: UUID = UUID.randomUUID()

    fun register(eventNode: EventNode<Event>) {

        // TODO optimization?

        val livingEventNode = EventNode.type("item-living-event", EventFilter.ENTITY)

        livingEventNode.listenOnly<EntityEquipEvent> {
            if (entity is LivingEntity) {
                refreshPlayerStats(entity as LivingEntity, mapOf(slot to equippedItem))
            }
        }

        livingEventNode.listenOnly<PlayerSwapItemEvent> {
            refreshPlayerStats(player,
                mapOf(EquipmentSlot.MAIN_HAND to mainHandItem,
                    EquipmentSlot.OFF_HAND to offHandItem)
            )
        }

        livingEventNode.listenOnly<PlayerChangeHeldSlotEvent> {
            refreshPlayerStats(player, mapOf(EquipmentSlot.MAIN_HAND to player.inventory.getItemStack(slot.toInt())))
        }

        livingEventNode.listenOnly<InventoryClickEvent> { refreshPlayerStats(player) }

        eventNode.addChild(livingEventNode)
    }

    fun refreshPlayerStats(entity: LivingEntity, changedSlots: Map<EquipmentSlot, ItemStack> = mapOf()) {
        val items: Map<EquipmentSlot, Item> = EquipmentSlot.values().mapNotNull {
                val item = ((changedSlots[it]?.meta?.get<Item>(Item.key, module)) ?: entity.getEquipment(it).meta.get(Item.key, module)) ?: return@mapNotNull null
                it to item
            }.toMap()

        val health = items.values.mapNotNull { it.get<HealthStatTrait>()?.value }.sum()

        val speed = items.values.mapNotNull { it.get<SpeedStatTrait>()?.value }.sum()

        entity.getAttribute(Attribute.MAX_HEALTH).removeModifier(AttributeModifier(healthUUID, healthStat, health, AttributeOperation.ADDITION))
        entity.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(AttributeModifier(speedUUID, speedStat, speed, AttributeOperation.MULTIPLY_BASE))

        entity.getAttribute(Attribute.MAX_HEALTH).addModifier(AttributeModifier(healthUUID, healthStat, health, AttributeOperation.ADDITION))
        entity.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(AttributeModifier(speedUUID, speedStat, speed, AttributeOperation.MULTIPLY_BASE))

    }
}
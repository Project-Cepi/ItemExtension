package world.cepi.itemextension.stats

import net.minestom.server.attribute.Attribute
import net.minestom.server.attribute.AttributeModifier
import net.minestom.server.attribute.AttributeOperation
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.item.ArmorEquipEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket
import world.cepi.itemextension.Handler
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.module
import world.cepi.itemextension.item.traits.list.stats.HealthStatTrait
import world.cepi.itemextension.item.traits.list.stats.SpeedStatTrait
import world.cepi.kstom.addEventCallback
import world.cepi.kstom.item.get
import java.util.*

object StatsHandler : Handler {

    const val healthStat = "health_item"
    val healthUUID: UUID = UUID.randomUUID()

    const val speedStat = "speed_item"
    val speedUUID: UUID = UUID.randomUUID()

    override fun register(playerInit: Player) {

        // TODO optimization?

        playerInit.addEventCallback<ArmorEquipEvent> {
            if (entity is LivingEntity) {
                refreshPlayerStats(entity as LivingEntity, mapOf(armorSlot.toEquipmentSlot() to armorItem))
            }
        }

        playerInit.addEventCallback<ItemDropEvent> { refreshPlayerStats(player) }

        playerInit.addEventCallback<PlayerSwapItemEvent> {
            refreshPlayerStats(player,
                mapOf(EntityEquipmentPacket.Slot.MAIN_HAND to mainHandItem,
                EntityEquipmentPacket.Slot.OFF_HAND to offHandItem)
            )
        }

        playerInit.addEventCallback<PlayerChangeHeldSlotEvent> {
            refreshPlayerStats(playerInit, mapOf(EntityEquipmentPacket.Slot.MAIN_HAND to player.inventory.getItemStack(slot.toInt())))
        }

        playerInit.addEventCallback<InventoryClickEvent> { refreshPlayerStats(playerInit) }
    }

    fun refreshPlayerStats(entity: LivingEntity, changedSlots: Map<EntityEquipmentPacket.Slot, ItemStack> = mapOf()) {
        val items: Map<EntityEquipmentPacket.Slot, Item> = EntityEquipmentPacket.Slot.values().mapNotNull {
                val item = ((changedSlots[it]?.meta?.get<Item>(Item.key, module)) ?: entity.getEquipment(it).meta.get(Item.key, module)) ?: return@mapNotNull null
                it to item
            }.toMap()

        val health = items.values.mapNotNull { it.getTrait<HealthStatTrait>()?.value }.sum()

        val speed = items.values.mapNotNull { it.getTrait<SpeedStatTrait>()?.value }.sum()

        entity.getAttribute(Attribute.MAX_HEALTH).removeModifier(AttributeModifier(healthUUID, healthStat, health, AttributeOperation.ADDITION))
        entity.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(AttributeModifier(speedUUID, speedStat, speed, AttributeOperation.MULTIPLY_BASE))

        entity.getAttribute(Attribute.MAX_HEALTH).addModifier(AttributeModifier(healthUUID, healthStat, health, AttributeOperation.ADDITION))
        entity.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(AttributeModifier(speedUUID, speedStat, speed, AttributeOperation.MULTIPLY_BASE))

    }
}

fun ArmorEquipEvent.ArmorSlot.toEquipmentSlot(): EntityEquipmentPacket.Slot {
    return when (this) {
        ArmorEquipEvent.ArmorSlot.BOOTS -> EntityEquipmentPacket.Slot.BOOTS
        ArmorEquipEvent.ArmorSlot.LEGGINGS -> EntityEquipmentPacket.Slot.LEGGINGS
        ArmorEquipEvent.ArmorSlot.CHESTPLATE -> EntityEquipmentPacket.Slot.CHESTPLATE
        ArmorEquipEvent.ArmorSlot.HELMET -> EntityEquipmentPacket.Slot.HELMET
    }
}
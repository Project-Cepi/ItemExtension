package world.cepi.itemextension.stats

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
import world.cepi.itemextension.item.traits.list.stats.HealthStatTrait
import world.cepi.itemextension.item.traits.list.stats.SpeedStatTrait
import world.cepi.kstom.addEventCallback

object StatsHandler : Handler {

    override fun register(playerInit: Player) {

        // TODO optimization?

        playerInit.addEventCallback<ArmorEquipEvent> { refreshPlayerStats(playerInit, armorSlot.toEquipmentSlot() to armorItem) }

        playerInit.addEventCallback<ItemDropEvent> { refreshPlayerStats(playerInit) }

        playerInit.addEventCallback<PlayerSwapItemEvent> {
            refreshPlayerStats(playerInit,
                EntityEquipmentPacket.Slot.MAIN_HAND to mainHandItem,
                EntityEquipmentPacket.Slot.OFF_HAND to offHandItem
            )
        }

        playerInit.addEventCallback<PlayerChangeHeldSlotEvent> {
            refreshPlayerStats(playerInit, EntityEquipmentPacket.Slot.MAIN_HAND to player.inventory.getItemStack(slot.toInt()))
        }

        playerInit.addEventCallback<InventoryClickEvent> { refreshPlayerStats(playerInit) }
    }

    fun refreshPlayerStats(player: Player, vararg changedSlots: Pair<EntityEquipmentPacket.Slot, ItemStack> = emptyArray()) {
        val items: MutableMap<EntityEquipmentPacket.Slot, Item> = EntityEquipmentPacket.Slot.values()
            .mapNotNull {
                val item = player.getEquipment(it).data?.get<Item>(Item.key) ?: return@mapNotNull null
                it to item
            }.toMap().toMutableMap()

        changedSlots.forEach {
            val item = it.second.data?.get<Item>(Item.key) ?: run {
                items.remove(it.first)
                return@forEach
            }

            items[it.first] = item
        }

        val health = items.values.mapNotNull { it.getTrait<HealthStatTrait>()?.value }.sum()

        // TODO speed
        val speed = items.values.mapNotNull { it.getTrait<SpeedStatTrait>()?.value }.sum()

        player.additionalHearts = health.toFloat()
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
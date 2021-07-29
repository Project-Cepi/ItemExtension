package world.cepi.itemextension.combat

import net.minestom.server.entity.Entity
import net.minestom.server.item.ItemStack
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.traits.list.AttackSpeedTrait

class ItemLastUse(val item: ItemStack, val time: Long = System.currentTimeMillis())

object AttackSpeedHandler {

    private val attackSpeedMap = mutableMapOf<Entity, ItemLastUse>()

    fun Entity.useAttackSpeed(itemStack: ItemStack) {
        // If the item isnt a cepi item they can attack
        val cepiItem = itemStack.cepiItem ?: return

        // If the item doesnt have attack speed they can use this item instantly
        if (!cepiItem.hasTrait<AttackSpeedTrait>()) return

        attackSpeedMap[this] = ItemLastUse(itemStack)
    }

    fun Entity.canUseItem(item: ItemStack): Boolean {

        // If the item isnt a cepi item they can attack
        val cepiItem = item.cepiItem ?: return true

        // If the item doesnt have attack speed they can use this item instantly
        if (!cepiItem.hasTrait<AttackSpeedTrait>()) return true

        // Make sure the NBT exists
        if (attackSpeedMap[this] == null) {
            useAttackSpeed(item)
            return false
        }

        // If the items are not the same this means they switched the item
        if (attackSpeedMap[this]!!.item != item) {
            useAttackSpeed(item)
            return false
        }

        // Has the cooldown time passed
        if (
            System.currentTimeMillis() - attackSpeedMap[this]!!.time
            < cepiItem.get<AttackSpeedTrait>()!!.attackSpeed.toMillis()
        ) return false

        return true
    }



}
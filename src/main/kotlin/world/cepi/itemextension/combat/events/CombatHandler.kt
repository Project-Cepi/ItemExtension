package world.cepi.itemextension.combat.events

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.hologram.Hologram
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.inventory.EquipmentHandler
import net.minestom.server.item.ItemStack
import net.minestom.server.sound.SoundEvent
import net.minestom.server.tag.Tag
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.combat.AttackSpeedHandler.canUseItem
import world.cepi.itemextension.combat.AttackSpeedHandler.useAttackSpeed
import world.cepi.itemextension.combat.ImmunityHandler
import world.cepi.itemextension.combat.util.applyKnockback
import world.cepi.itemextension.item.cepiItem
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.*
import world.cepi.kstom.Manager
import java.text.NumberFormat

object CombatHandler {

    fun register(event: EntityAttackEvent) = with(event) {
        // Don't action players in creative!
        if (target is Player && (target as Player).gameMode == GameMode.CREATIVE) {
            return
        }

        // Stop dead players from being hit
        if (entity.isDeadCepi || target.isDeadCepi)
            return

        // Don't attack immume mobs (red delay)
        if (ImmunityHandler.isImmune(target))
            return

        // Don't attack immune mobs (by tag)
        if (target.hasTag(Tag.Byte("invulnerable")))
            return

        if (entity is EquipmentHandler && !entity.canUseItem((entity as EquipmentHandler).itemInMainHand))
            return

        // Find the player's item if any
        val item = (entity as? LivingEntity)?.itemInMainHand

        // Find the player's item as a cepi item if any
        val cepiItem = if (item != null && checkIsItem(item)) {
            item.cepiItem
        } else null

        // Ensure players only use tools at their level
        if (entity is Player) {

            // Don't action if the player doesn't have the correct levels

            cepiItem?.get<LevelTrait>()?.let {
                if ((entity as Player).level < it.level) return
            }
        }

        // Ensure only living entities
        if (target !is LivingEntity) return@with

        val livingTarget = target as LivingEntity

        // Get the damage of the item, 1 if the user isn't holding a cepi item.
        val damage: Double = cepiItem?.get<DamageTrait>()?.damage ?: 1.0

        // Appends armor to the damage. Collects all armor from all armor slots and applies it as so.
        val armor = listOf(
            livingTarget.boots,
            livingTarget.leggings,
            livingTarget.chestplate,
            livingTarget.helmet
        ).map {
            it.cepiItem?.get<ArmorTrait>()?.armor ?: 0.0
        }.sum()

        val finalDamage = ArmorTrait.applyToDamage(armor, damage)

        // Damage the entity
        livingTarget.damage(DamageType.fromEntity(livingTarget), finalDamage.toFloat())

        // Apply knockback to the entity
        applyKnockback(target, entity, cepiItem?.get<KnockbackTrait>()?.amount ?: 1.0f)

        // Display the holograms
        run {
            val format = NumberFormat.getInstance().format(-finalDamage)

            val hologram = Hologram(
                livingTarget.instance!!,
                livingTarget.position.add(0.0, livingTarget.eyeHeight, 0.0),
                Component.text(format, NamedTextColor.RED).append(Component.text(" ❤", NamedTextColor.RED)),
                true,
                true
            )

            Manager.scheduler.buildTask {
                hologram.remove()
            }.delay(1, TimeUnit.SECOND).schedule()
        }

        // Calls the event for other handlers to listen to.
        val deathEvent = EntityDeathEvent(target)
        Manager.globalEvent.call(deathEvent)

        if (cepiItem?.hasTrait<DurabilityTrait>() == true) {

            val currentDurabilityTrait = cepiItem.get<DurabilityTrait>()!!

            if (currentDurabilityTrait.currentDurability - 1 <= 0) {
                (entity as? LivingEntity)?.itemInMainHand = ItemStack.AIR

                (entity as? Player)?.playSound(
                    Sound.sound(
                        SoundEvent.ENTITY_ITEM_BREAK,
                        Sound.Source.MASTER,
                        1f,
                        1f
                    )
                )
            } else {
                cepiItem.put(DurabilityTrait(
                    currentDurabilityTrait.maxDurability,
                    currentDurabilityTrait.currentDurability - 1)
                )

                run set@ {
                    (entity as? LivingEntity)?.itemInMainHand = cepiItem.renderItem(
                        (entity as? LivingEntity)?.itemInMainHand?.amount ?: return@set
                    )
                }
            }
        }

        // Trigger immunity and attack speed if applicable
        ImmunityHandler.triggerImmune(target)

        if (entity is EquipmentHandler)
            entity.useAttackSpeed((entity as EquipmentHandler).itemInMainHand)

    }


}
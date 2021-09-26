package world.cepi.itemextension.combat.events

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.damage.EntityDamage
import net.minestom.server.entity.hologram.Hologram
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.entity.EntityDamageEvent
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

    /**
     * Check if an [Entity] can be damaged
     *
     * @return true if it can be damaged, false otherwise
     */
    fun canBeDamaged(entity: Entity): Boolean {

        // Don't action players in creative!
        if (entity is Player && entity.gameMode == GameMode.CREATIVE) {
            return false
        }

        // Stop dead players from being hit
        if (entity.isDeadCepi)
            return false

        // Don't attack immume mobs (red delay)
        if (ImmunityHandler.isImmune(entity))
            return false

        // Don't attack immune mobs (by tag)
        if (entity.hasTag(Tag.Byte("invulnerable")))
            return false

        return true
    }

    /**
     * Check if an [Entity] can damage another entity
     *
     * @return true if an entity can be damaged, false otherwise
     */
    fun canDamageEntities(entity: Entity): Boolean {
        // Stop dead entities from hitting players
        if (entity.isDeadCepi || entity.isRemoved)
            return false

        if (entity is EquipmentHandler && !entity.canUseItem(entity.itemInMainHand))
            return false

        return true
    }

    fun registerGenericDamage(event: EntityDamageEvent): Unit = with(event) {
        if (!canBeDamaged(entity)) {
            isCancelled = true
            return
        }

        if (damageType is EntityDamage) {

            val entityDamage = damageType as EntityDamage

            if (!canDamageEntities(entityDamage.source)) {
                isCancelled = true
                return
            }

            // Ensure players only use tools at their level
            if (entityDamage.source is Player) {

                val attacker = entityDamage.source as Player

                // Find the player's item if any
                val item = (attacker as? LivingEntity)?.itemInMainHand

                // Find the player's item as a cepi item if any
                val cepiItem = if (item != null && checkIsItem(item)) {
                    item.cepiItem
                } else null

                // Don't action if the player doesn't have the correct levels

                cepiItem?.get<LevelTrait>()?.let {
                    if (attacker.level < it.level) {
                        isCancelled = true
                        return
                    }
                }
            }

        }

        // Appends armor to the damage. Collects all armor from all armor slots and applies it as so.
        val armor = listOf(
            entity.boots,
            entity.leggings,
            entity.chestplate,
            entity.helmet
        ).map {
            it.cepiItem?.get<ArmorTrait>()?.armor ?: 0.0f
        }.sum()

        val finalDamage = ArmorTrait.applyToDamage(armor, damage)

        damage = finalDamage

        // Display the holograms
        run {
            val format = NumberFormat.getInstance().format(-finalDamage)

            val hologram = Hologram(
                entity.instance!!,
                entity.position.add(0.0, entity.eyeHeight, 0.0),
                Component.text(format, NamedTextColor.RED).append(Component.text(" ❤", NamedTextColor.RED)),
                true,
                true
            )

            Manager.scheduler.buildTask {
                hologram.remove()
            }.delay(1, TimeUnit.SECOND).schedule()
        }

        ImmunityHandler.triggerImmune(entity)
    }

    fun registerDamageByEntity(event: EntityAttackEvent) = with(event) {
        if (!canDamageEntities(entity)) {
            return
        }

        // Ensure only living entities
        if (target !is LivingEntity) return@with

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

        // Damage the entity
        (target as LivingEntity).damage(DamageType.fromEntity(entity), cepiItem?.get<DamageTrait>()?.damage ?: 1.0f)

        // Apply knockback to the entity
        applyKnockback(target, entity, cepiItem?.get<KnockbackTrait>()?.amount ?: 1.0f)

        if (entity is EquipmentHandler)
            entity.useAttackSpeed((entity as EquipmentHandler).itemInMainHand)

    }


}
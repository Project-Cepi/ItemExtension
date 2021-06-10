package world.cepi.itemextension.combat.events

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.hologram.Hologram
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.item.Material
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.combat.ImmunityHandler
import world.cepi.itemextension.combat.util.applyKnockback
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.module
import world.cepi.itemextension.item.traits.list.ArmorTrait
import world.cepi.itemextension.item.traits.list.DamageTrait
import world.cepi.itemextension.item.traits.list.LevelTrait
import world.cepi.kstom.addEventCallback
import world.cepi.kstom.item.get
import java.text.NumberFormat


object CombatHandler {

    fun register() {
        MinecraftServer.getGlobalEventHandler().addEventCallback<EntityAttackEvent> {
            // Don't attack players in creative!
            if (target is Player && (target as Player).gameMode == GameMode.CREATIVE) {
                return@addEventCallback
            }

            // Stop dead players from being hit
            if (DeathHandler.deadPlayers.contains(entity) || DeathHandler.deadPlayers.contains(target))
                return@addEventCallback

            if (ImmunityHandler.isImmune(target))
                return@addEventCallback

            // Find the player's item if any
            val item = (entity as? LivingEntity)?.itemInMainHand

            // Find the player's item as a cepi item if any
            val cepiItem = if (item != null && checkIsItem(item)) {
                item.meta.get<Item>(Item.key, module)!!
            } else null

            if (entity is Player) {

                // Don't attack if the player doesn't have the correct levels

                cepiItem?.get<LevelTrait>()?.let {
                    if ((entity as Player).level < it.level) return@addEventCallback
                }
            }

            if (target is LivingEntity) {

                val livingTarget = target as LivingEntity

                // Get the damage of the item, 1 if the user isn't holding an item.
                val damage: Double = if (item?.material == Material.AIR)
                    1.0
                else
                    cepiItem?.get<DamageTrait>()?.damage ?: 1.0

                // Appends armor to the damage. Collects all armor from all armor slots and applies it as so.
                val armor = listOf(
                    livingTarget.boots,
                    livingTarget.leggings,
                    livingTarget.chestplate,
                    livingTarget.helmet
                ).map {
                    it.meta.get<Item>(Item.key, module)?.get<ArmorTrait>()?.armor ?: 0.0
                }.sum()

                val finalDamage = ArmorTrait.applyToDamage(armor, damage)

                // TODO attack speed?

                livingTarget.damage(DamageType.fromEntity(livingTarget), finalDamage.toFloat())
                applyKnockback(target, entity)

                val format = NumberFormat.getInstance().format(-finalDamage)

                val hologram = Hologram(
                        livingTarget.instance,
                        livingTarget.position.clone().add(0.0, 1.0, 0.0),
                        Component.text(format, NamedTextColor.RED),
                        true
                )

                MinecraftServer.getSchedulerManager().buildTask {
                    hologram.remove()
                }.delay(1, TimeUnit.SECOND).schedule()

            }

            // Calls the event for other handlers to listen to.
            val deathEvent = EntityDeathEvent(target)
            target.callEvent(EntityDeathEvent::class.java, deathEvent)

            ImmunityHandler.triggerImmune(target)
        }


    }

}
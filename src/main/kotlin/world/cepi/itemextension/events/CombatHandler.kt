package world.cepi.itemextension.events

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.hologram.Hologram
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.item.Material
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.combat.util.applyKnockback
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.checkIsItem
import world.cepi.itemextension.item.traits.list.ArmorTrait
import world.cepi.itemextension.item.traits.list.DamageTrait
import world.cepi.itemextension.item.traits.list.LevelTrait
import java.text.NumberFormat


object CombatHandler : Handler {

    override fun register(playerInit: Player) {
        MinecraftServer.getGlobalEventHandler().addEventCallback(EntityAttackEvent::class.java) { entityAttackEvent ->
            with(entityAttackEvent) {

                // Don't attack players in creative!
                if (target is Player && (target as Player).gameMode == GameMode.CREATIVE) {
                    return@addEventCallback
                }

                val livingEntitySource = entity as LivingEntity

                if (DeathHandler.isDead.contains(livingEntitySource) || DeathHandler.isDead.contains(target))
                    return@addEventCallback

                val item = livingEntitySource.itemInMainHand

                val cepiItem = if (checkIsItem(item)) {
                    item.data!!.get<Item>(Item.key)!!
                } else null

                if (entity is Player) {

                    // Don't attack if the player doesn't have the correct levels

                    cepiItem?.getTrait<LevelTrait>()?.let {
                        if ((entity as Player).level < it.level) return@addEventCallback
                    }
                }

                if (target is LivingEntity) {

                    val entity = target as LivingEntity

                    // Get the damage of the item, 1 if the user isn't holding an item.
                    val damage: Double = if (item.material == Material.AIR)
                        1.0
                    else
                        cepiItem?.getTrait<DamageTrait>()?.damage ?: 1.0

                    // Appends armor to the damage. Collects all armor from all armor slots and applies it as so.
                    val armor = listOf(entity.boots, entity.leggings, entity.chestplate, entity.helmet).map {
                        it.data?.get<Item>(Item.key)?.getTrait<ArmorTrait>()?.armor ?: 0.0
                    }.sum()

                    val finalDamage = ArmorTrait.applyToDamage(armor, damage)

                    // TODO attack speed?

                    entity.damage(DamageType.fromEntity(livingEntitySource), finalDamage.toFloat())
                    applyKnockback(entity, livingEntitySource)

                    val format = NumberFormat.getInstance().format(-finalDamage)

                    val hologram = Hologram(
                            entity.instance,
                            entity.position.clone().add(0.0, 1.0, 0.0),
                            ColoredText.of("${ChatColor.RED}$format"),
                            true
                    )

                    MinecraftServer.getSchedulerManager().buildTask {
                        hologram.remove()
                    }.delay(1, TimeUnit.SECOND).schedule()

                }

                // Calls the event for other handlers to listen to.
                val deathEvent = EntityDeathEvent(target)
                target.callEvent(EntityDeathEvent::class.java, deathEvent)
            }
        }

    }

}
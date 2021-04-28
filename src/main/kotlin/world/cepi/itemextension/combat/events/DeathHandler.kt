package world.cepi.itemextension.combat.events

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityDamageEvent
import net.minestom.server.instance.Instance
import net.minestom.server.sound.SoundEvent
import net.minestom.server.utils.Position
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.Handler
import world.cepi.kstom.addEventCallback


object DeathHandler : Handler {
    val isDead = mutableListOf<Player>()

    private fun deathMessage(player: Player, secondsLeft: Int) {
        player.showTitle(Title.title(
            Component.text("You died!", NamedTextColor.RED),
            Component.text("Respawning in $secondsLeft...", NamedTextColor.WHITE),
            Title.Times.of(Ticks.duration(0), Ticks.duration(21), Ticks.duration(0))
        ))
    }

    override fun register(playerInit: Player) {
        val schedulerManager = MinecraftServer.getSchedulerManager()
        playerInit.addEventCallback<EntityDamageEvent> {
            if (entity is Player && damage >= entity.health) {
                isCancelled = true
                val player = entity as Player
                val originalGamemode = player.gameMode
                val originalFlyStatus = player.isAllowFlying

                player.gameMode = GameMode.ADVENTURE
                player.heal()
                player.food = 20
                player.isAllowFlying = true
                player.isInvisible = true

                getNearbyEntities(player.position, 10.0, player.instance!!).filterIsInstance<Player>().forEach { loopPlayer ->
                    loopPlayer.playSound(Sound.sound(
                        SoundEvent.ENTITY_SKELETON_DEATH, Sound.Source.PLAYER,2F, 1F
                    ), player.position.x, player.position.y, player.position.z)
                }

                deathMessage(player, 3)

                isDead.add(player)

                schedulerManager.buildTask { deathMessage(player, 2) }.delay(1, TimeUnit.SECOND).schedule()
                schedulerManager.buildTask { deathMessage(player, 1) }.delay(2, TimeUnit.SECOND).schedule()
                schedulerManager.buildTask {
                    player.resetTitle()
                    player.teleport(player.respawnPoint)
                    player.gameMode = originalGamemode
                    isDead.remove(player)
                    player.isAllowFlying = originalFlyStatus
                    player.isFlying = false
                    player.isInvisible = false
                }.delay(3, TimeUnit.SECOND).schedule()
            }
        }

    }

    private fun getNearbyEntities(location: Position, distance: Double, instance: Instance): Collection<Entity> {
        return instance.entities.filter { e -> e.position.getDistance(location) <= distance * distance }
    }
}
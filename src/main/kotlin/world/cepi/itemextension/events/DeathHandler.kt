package world.cepi.itemextension.events

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityDamageEvent
import net.minestom.server.instance.Instance
import net.minestom.server.sound.Sound
import net.minestom.server.sound.SoundCategory
import net.minestom.server.utils.Position
import net.minestom.server.utils.time.TimeUnit


object DeathHandler : Handler {
    val isDead = mutableListOf<Player>()

    private fun deathMessage(player: Player, secondsLeft: Int) {
        player.sendTitleSubtitleMessage(
            ColoredText.of(ChatColor.RED, "You died!"),
            ColoredText.of("Respawning in $secondsLeft...")
        )
        player.sendTitleTime(0, 21, 0)
    }

    override fun register(playerInit: Player) {
        val schedulerManager = MinecraftServer.getSchedulerManager()
        playerInit.addEventCallback(EntityDamageEvent::class.java) {
            with(it) {
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
                        loopPlayer.playSound(
                            Sound.ENTITY_SKELETON_DEATH, SoundCategory.PLAYERS,
                            loopPlayer.position.x.toInt(), loopPlayer.position.y.toInt(), loopPlayer.position.z.toInt(),
                            2F, 1F
                        )
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
    }

    private fun getNearbyEntities(location: Position, distance: Double, instance: Instance): Collection<Entity> {
        return instance.entities.filter { e -> e.position.getDistance(location) <= distance * distance }
    }
}
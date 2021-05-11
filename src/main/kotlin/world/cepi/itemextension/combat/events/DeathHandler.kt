package world.cepi.itemextension.combat.events

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityDamageEvent
import net.minestom.server.instance.Instance
import net.minestom.server.sound.SoundEvent
import net.minestom.server.utils.Position
import net.minestom.server.utils.time.TimeUnit
import world.cepi.itemextension.Handler
import world.cepi.kstom.Manager
import world.cepi.kstom.addEventCallback


object DeathHandler : Handler {
    val deadPlayers = mutableListOf<Player>()

    private fun deathMessage(player: Player, secondsLeft: Int) = player.showTitle(Title.title(
        Component.text("You died!", NamedTextColor.RED),
        Component.text("Respawning in $secondsLeft...", NamedTextColor.WHITE),
        Title.Times.of(Ticks.duration(0), Ticks.duration(21), Ticks.duration(0))
    ))


    override fun register(playerInit: Player) {
        playerInit.addEventCallback<EntityDamageEvent> {

            // Looking for only players
            if (entity !is Player) return@addEventCallback

            // Damage must be higher than the health
            if (damage < entity.health) return@addEventCallback

            // If both conditions passed, cancel the event
            isCancelled = true

            val player = entity as Player

            // Cache variables for gamemode / flying
            val originalGamemode = player.gameMode
            val originalFlyStatus = player.isAllowFlying

            with(player) {
                gameMode = GameMode.ADVENTURE
                heal()
                food = 20
                isAllowFlying = true
                isInvisible = true
            }

            getNearbyEntities(
                player.instance!!, player.position,
                10.0
            ).filterIsInstance<Player>()
                .forEach { loopPlayer ->
                    loopPlayer.playSound(Sound.sound(
                        SoundEvent.ENTITY_SKELETON_DEATH, Sound.Source.PLAYER,2F, 1F
                    ), player.position.x, player.position.y, player.position.z)
                }

            deathMessage(player, 3)

            deadPlayers.add(player)

            Manager.scheduler.buildTask { deathMessage(player, 2) }.delay(1, TimeUnit.SECOND).schedule()
            Manager.scheduler.buildTask { deathMessage(player, 1) }.delay(2, TimeUnit.SECOND).schedule()
            Manager.scheduler.buildTask {
                with(player) {
                    resetTitle()
                    teleport(player.respawnPoint)

                    gameMode = originalGamemode
                    isAllowFlying = originalFlyStatus
                    isFlying = false
                    isInvisible = false
                }

                deadPlayers.remove(player)
            }.delay(3, TimeUnit.SECOND).schedule()
        }

    }

    private fun getNearbyEntities(instance: Instance, location: Position, distance: Double): Collection<Entity> {
        return instance.entities.filter { e -> e.position.getDistance(location) <= distance * distance }
    }
}
package world.cepi.itemextension.item.traits.list.actions.list

import kotlinx.serialization.Serializable
import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.play.SetCooldownPacket
import net.minestom.server.sound.SoundEvent
import world.cepi.itemextension.item.Item
import world.cepi.itemextension.item.traits.list.MaterialTrait
import world.cepi.kstom.serializer.SoundEventSerializer
import kotlin.random.Random

@Serializable
data class RandomInstrument(
    @Serializable(with = SoundEventSerializer::class)
    val sound: SoundEvent
) : Action() {

    override val displayName = "Instrument"

    override val requiresTarget = false

    override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
        player.playSound(
            Sound.sound(
            sound,
            Sound.Source.MASTER,
            1f,
            Random.nextDouble(0.5, 2.0).toFloat()
        ))

        val material = item.get<MaterialTrait>()?.material ?: return false

        val packet = SetCooldownPacket().apply {
            itemId = material.id()
            cooldownTicks = 20
        }

        player.sendPacket(packet)

        return false
    }
}
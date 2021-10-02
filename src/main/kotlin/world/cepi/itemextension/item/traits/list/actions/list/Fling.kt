package world.cepi.itemextension.item.traits.list.actions.list

import kotlinx.serialization.Serializable
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item

@Serializable
data class Fling(val velocity: Double = 20.0) : Action() {
    override val displayName = "Fling"

    override val requiresTarget = true

    override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean {
        target!!.velocity.add(0.0, velocity, 0.0)

        return true
    }
}
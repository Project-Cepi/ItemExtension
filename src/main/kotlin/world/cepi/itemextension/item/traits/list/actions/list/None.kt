package world.cepi.itemextension.item.traits.list.actions.list

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import world.cepi.itemextension.item.Item

@Serializable
class None: Action() {
    @Transient
    override val displayName = "None"

    override fun invoke(player: Player, target: LivingEntity?, item: Item): Boolean = false

    override fun hashCode() = 0

    override fun equals(other: Any?): Boolean {
        if (other !is None) return false
        return true
    }
}
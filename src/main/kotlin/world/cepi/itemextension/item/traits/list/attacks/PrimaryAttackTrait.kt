package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.KItem

@Serializable
@SerialName("primary_attack")
class PrimaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Left"

    override val loreIndex = 2.1f

    override fun task(item: ItemStackBuilder) {
        item.leftCallbacks.add { player, hand ->

            if (!player.isSneaking)
                return@add attack.action(player, hand)

            return@add true

        }
    }
}
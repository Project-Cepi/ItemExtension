package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.KItem

@Serializable
@SerialName("secondary_attack")
class SecondaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Sneak + Left"

    override val loreIndex = 2.2f

    override fun task(item: ItemStackBuilder) {
        item.leftCallbacks.add { player, hand ->

            if (player.isSneaking)
                return@add attack.action(player, hand)

            return@add true

        }
    }

}
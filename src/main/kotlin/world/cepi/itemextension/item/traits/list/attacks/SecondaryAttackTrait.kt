package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.KItem

@Serializable
@SerialName("secondary_attack")
class SecondaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Shift + Left"

    override val loreIndex = 2.2f

    override fun task(item: KItem) {
        item.leftCallbacks.add { player, hand ->

            if (player.isSneaking)
                return@add attack.action(player, hand)

            return@add true

        }
    }

}
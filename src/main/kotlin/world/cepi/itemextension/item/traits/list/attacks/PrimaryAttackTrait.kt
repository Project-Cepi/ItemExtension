package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemStackBuilder
import world.cepi.itemextension.item.Item

@Serializable
@SerialName("primary_attack")
data class PrimaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Left"

    override fun task(item: ItemStackBuilder, originalItem: Item) {
//        item.leftCallbacks.add { player, hand ->
//
//            if (!player.isSneaking)
//                return@add attack.action(player, hand)
//
//            return@add true
//
//        }
    }
}
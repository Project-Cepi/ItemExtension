package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.KItem

@Serializable
@SerialName("primary_attack")
class PrimaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Left"

    override val loreIndex = 2.1f

    override fun task(item: KItem) {
        item.leftCallbacks.add(attack.action)
    }
}
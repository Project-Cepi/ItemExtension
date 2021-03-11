package world.cepi.itemextension.item.traits.list.attacks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.KItem

@Serializable
@SerialName("tertiary_attack")
class TertiaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Right"

    override val loreIndex = 2.3f

    override fun task(item: KItem) {
        item.rightCallbacks.add(attack.action)
    }

}
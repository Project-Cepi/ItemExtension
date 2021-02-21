package world.cepi.itemextension.item.traits.list.attacks

import world.cepi.itemextension.item.KItem

class TertiaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Right"

    override val loreIndex = 2.3f

    override fun task(item: KItem) {
        item.rightCallbacks.add(attack.action)
    }

}
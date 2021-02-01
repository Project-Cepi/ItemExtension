package world.cepi.itemextension.item.traits.list.attacks

import world.cepi.itemextension.item.KItem

class PrimaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Left"

    override fun task(item: KItem) {
        item.leftCallbacks.add(attack.action)
    }
}
package world.cepi.itemextension.item.traits.list.attacks

import world.cepi.itemextension.item.KItem

class SecondaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Shift + Left"

    override fun task(item: KItem) {
        item.leftCallbacks.add { player, hand ->

            if (player.isSneaking)
                return@add attack.action(player, hand)

            return@add true

        }
    }

}
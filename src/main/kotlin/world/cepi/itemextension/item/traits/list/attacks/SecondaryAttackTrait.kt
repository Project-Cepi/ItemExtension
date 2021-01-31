package world.cepi.itemextension.item.traits.list.attacks

class SecondaryAttackTrait(override val attack: Attack): AttackTrait() {
    override val clickType = "Shift + Left"
}
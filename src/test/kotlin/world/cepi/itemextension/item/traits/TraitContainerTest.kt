package world.cepi.itemextension.item.traits

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import world.cepi.itemextension.item.traits.list.NameTrait
import world.cepi.itemextension.item.traits.list.attacks.Attack
import world.cepi.itemextension.item.traits.list.attacks.AttackTrait
import world.cepi.itemextension.item.traits.list.attacks.PrimaryAttackTrait
import world.cepi.itemextension.item.traits.list.attacks.SecondaryAttackTrait

class TraitContainerTest {

    class TraitContainerImpl: TraitContainer<ItemTrait>()

    @Test
    fun `TraitContainer should correctly handle trait storage`() {
        val traitContainerImpl = TraitContainerImpl()

        traitContainerImpl.put(NameTrait("Hello World!"))

        traitContainerImpl.put(PrimaryAttackTrait(Attack.TargetAttack()))
        traitContainerImpl.put(SecondaryAttackTrait(Attack.Strike()))

        assertTrue(traitContainerImpl.hasTrait<NameTrait>())

        assertFalse(traitContainerImpl.hasTrait<AttackTrait>())

        assertTrue(traitContainerImpl.hasTrait<PrimaryAttackTrait>())
        assertTrue(traitContainerImpl.softHasTrait<AttackTrait>())

        assertEquals(
            listOf(
                PrimaryAttackTrait(Attack.TargetAttack()),
                SecondaryAttackTrait(Attack.Strike())
            ),
            traitContainerImpl.softGet<AttackTrait>()
        )

        assertEquals("Hello World!", traitContainerImpl.get<NameTrait>()?.name)

        traitContainerImpl.removeTrait<PrimaryAttackTrait>()

        assertFalse(traitContainerImpl.hasTrait<PrimaryAttackTrait>())
    }
}
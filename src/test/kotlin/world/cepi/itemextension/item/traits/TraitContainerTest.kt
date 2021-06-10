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

        traitContainerImpl.addTrait(NameTrait("Hello World!"))

        traitContainerImpl.addTrait(PrimaryAttackTrait(Attack.TARGET_ATTACK))
        traitContainerImpl.addTrait(SecondaryAttackTrait(Attack.STRIKE))

        assertTrue(traitContainerImpl.hasTrait<NameTrait>())

        assertFalse(traitContainerImpl.hasTrait<AttackTrait>())

        assertTrue(traitContainerImpl.hasTrait<PrimaryAttackTrait>())
        assertTrue(traitContainerImpl.softHasTrait<AttackTrait>())

        assertEquals(
            listOf(
                PrimaryAttackTrait(Attack.TARGET_ATTACK),
                SecondaryAttackTrait(Attack.STRIKE)
            ),
            traitContainerImpl.softGet<AttackTrait>()
        )

        assertEquals("Hello World!", traitContainerImpl.get<NameTrait>()?.name)
    }
}
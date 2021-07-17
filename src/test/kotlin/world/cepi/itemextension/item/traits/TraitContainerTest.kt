package world.cepi.itemextension.item.traits

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import world.cepi.itemextension.item.traits.list.NameTrait
import world.cepi.itemextension.item.traits.list.actions.Action
import world.cepi.itemextension.item.traits.list.actions.ActionTrait
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait
import world.cepi.itemextension.item.traits.list.actions.SecondaryActionTrait

class TraitContainerTest {

    class TraitContainerImpl: TraitContainer<ItemTrait>()

    @Test
    fun `TraitContainer should correctly handle trait storage`() {
        val traitContainerImpl = TraitContainerImpl()

        traitContainerImpl.put(NameTrait("Hello World!"))

        traitContainerImpl.put(PrimaryActionTrait(Action.FlatDamage()))
        traitContainerImpl.put(SecondaryActionTrait(Action.Strike()))

        assertTrue(traitContainerImpl.hasTrait<NameTrait>())

        assertFalse(traitContainerImpl.hasTrait<ActionTrait>())

        assertTrue(traitContainerImpl.hasTrait<PrimaryActionTrait>())
        assertTrue(traitContainerImpl.softHasTrait<ActionTrait>())

        assertEquals(
            listOf(
                PrimaryActionTrait(Action.FlatDamage()),
                SecondaryActionTrait(Action.Strike())
            ),
            traitContainerImpl.softGet<ActionTrait>()
        )

        assertEquals("Hello World!", traitContainerImpl.get<NameTrait>()?.name)

        traitContainerImpl.removeTrait<PrimaryActionTrait>()

        assertFalse(traitContainerImpl.hasTrait<PrimaryActionTrait>())
    }
}
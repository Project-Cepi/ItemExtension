package world.cepi.itemextension.item.traits

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import world.cepi.actions.list.FlingAction
import world.cepi.itemextension.item.traits.list.NameTrait
import world.cepi.itemextension.item.traits.list.actions.ActionTrait
import world.cepi.itemextension.item.traits.list.actions.PrimaryActionTrait
import world.cepi.itemextension.item.traits.list.actions.SecondaryActionTrait

class TraitContainerTest {

    class TraitContainerImpl: TraitContainer<ItemTrait>()

    @Disabled
    fun `TraitContainer should correctly handle trait storage`() {
        val traitContainerImpl = TraitContainerImpl()

        traitContainerImpl.put(NameTrait("Hello World!"))

        traitContainerImpl.put(PrimaryActionTrait(FlingAction(1.0), "Fling", true))
        traitContainerImpl.put(SecondaryActionTrait(FlingAction(1.0), "Fling", true))

        assertTrue(traitContainerImpl.hasTrait<NameTrait>())

        assertFalse(traitContainerImpl.hasTrait<ActionTrait>())

        assertTrue(traitContainerImpl.hasTrait<PrimaryActionTrait>())
        assertTrue(traitContainerImpl.softHasTrait<ActionTrait>())

        assertEquals(
            listOf(
                PrimaryActionTrait(FlingAction(1.0), "Fling", true),
                SecondaryActionTrait(FlingAction(1.0), "Fling", true)
            ),
            traitContainerImpl.softGet<ActionTrait>()
        )

        assertEquals("Hello World!", traitContainerImpl.get<NameTrait>()?.name)

        traitContainerImpl.removeTrait<PrimaryActionTrait>()

        assertFalse(traitContainerImpl.hasTrait<PrimaryActionTrait>())
    }
}
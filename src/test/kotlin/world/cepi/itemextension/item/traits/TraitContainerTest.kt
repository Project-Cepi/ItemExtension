package world.cepi.itemextension.item.traits

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import world.cepi.actions.ActionItem
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

        traitContainerImpl.put(PrimaryActionTrait("Fling", true, ActionItem(FlingAction(1.0))))
        traitContainerImpl.put(SecondaryActionTrait("Fling", true, ActionItem(FlingAction(1.0))))

        assertTrue(traitContainerImpl.hasTrait<NameTrait>())

        assertFalse(traitContainerImpl.hasTrait<ActionTrait>())

        assertTrue(traitContainerImpl.hasTrait<PrimaryActionTrait>())
        assertTrue(traitContainerImpl.softHasTrait<ActionTrait>())

        assertEquals(
            listOf(
                PrimaryActionTrait("Fling", true, ActionItem(FlingAction(1.0))),
                SecondaryActionTrait("Fling", true, ActionItem(FlingAction(1.0)))
            ),
            traitContainerImpl.softGet<ActionTrait>()
        )

        assertEquals("Hello World!", traitContainerImpl.get<NameTrait>()?.name)

        traitContainerImpl.removeTrait<PrimaryActionTrait>()

        assertFalse(traitContainerImpl.hasTrait<PrimaryActionTrait>())
    }
}
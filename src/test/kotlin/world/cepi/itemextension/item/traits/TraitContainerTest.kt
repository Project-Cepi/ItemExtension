package world.cepi.itemextension.item.traits

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import world.cepi.itemextension.item.traits.list.NameTrait

class TraitContainerTest {

    class TraitContainerImpl: TraitContainer<Trait>() {
        override val traits: MutableList<Trait> = mutableListOf()
    }

    @Test
    fun `TraitContainer should correctly handle trait storage`() {
        val traitContainerImpl = TraitContainerImpl()
        traitContainerImpl.addTrait(NameTrait("Hello World!"))

        assert(traitContainerImpl.hasTrait(NameTrait::class))

        assertEquals(traitContainerImpl.getTrait<NameTrait>()?.name, "Hello World!")
    }
}
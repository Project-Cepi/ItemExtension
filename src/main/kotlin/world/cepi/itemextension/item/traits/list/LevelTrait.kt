package world.cepi.itemextension.item.traits.list

import world.cepi.itemextension.item.traits.Trait

/** The level amount required to even use the item, usually to define its overall quality. */
class LevelTrait(
    /** The minimum level required to use the item passed as a parameter. */
    private val level: Int
) : Trait {

    override val loreIndex = 0

}
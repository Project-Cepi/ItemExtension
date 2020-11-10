package world.cepi.itemextension.item.traits.list

import world.cepi.itemextension.item.traits.TraitContainer

/** Handles a unified form of stats. Contains traits */
class AttributeDisplayTrait : TraitContainer<AttributeTrait>, ItemTrait {

    override val traits: MutableList<AttributeTrait> = mutableListOf()

    override fun renderLore(): List<String> {
        return super.renderLore()
    }

}
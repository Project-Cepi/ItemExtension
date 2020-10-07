package world.cepi.itemextension.item

import net.minestom.server.item.Material
import world.cepi.itemextension.item.traits.Trait
import kotlin.reflect.KClass

/**
 * Item object wrapper for Cepi's items. Built on top of the decorator pattern, calling them traits.
 */
class Item(
    /**
     * The name of the item. Color is defiend by rarity, not the name
     */
    var name: String,

    /**
     * The material of the item. For display reasons. customModelData can be defined as a trait.
     */
    var material: Material,

    /**
     * The rarity of the Item.
     */
    var rarity: Rarity
) {

    /**
     * List of traits that can be added or removed from. Its modifier is private to prevent any accidental hacky situations
     */
    private val traits: MutableList<Trait> = mutableListOf()

    /**
     * Encapsulation function to add a trait to the [traits] property
     */
    fun addTrait(trait: Trait) {
        traits.add(trait)
    }

    /**
     * Safely removes a trait based on its class refrence.
     */
    fun removeTrait(traitClass: KClass<Trait>) {
        traits.filter { it::class == traitClass }.forEach { traits.remove(it) }
    }


}
package world.cepi.itemextension.item.traits

import kotlin.reflect.KClass

interface TraitContainer<T : Trait> {

    /**
     * List of traits that can be added or removed from.
     */
    val traits: MutableList<T>

    /**
     * Encapsulation function to add a trait to the [traits] property
     */
    fun addTrait(trait: T) {
        traits.add(trait)
    }

    /**
     * Safely removes a trait based on its class refrence.
     */
    fun removeTrait(traitClass: KClass<T>) {
        traits.filter { it::class == traitClass }.forEach { traits.remove(it) }
    }

}
package world.cepi.itemextension.item.traits

import kotlin.reflect.KClass

interface TraitContainer<T : Trait> {

    /**
     * List of traits that can be added or removed from.
     */
    val traits: MutableList<T>

    /**
     * Encapsulation function to add a trait to the [traits] property
     *
     * @param trait The trait to add
     *
     * @return The return value will always be true
     */
    fun addTrait(trait: T): Boolean = traits.add(trait)

    /**
     * Safely removes a trait based on its class refrence.
     *
     * @param traitClass The class remove from the array.
     */
    fun removeTrait(traitClass: KClass<out T>) = traits.filter { it::class == traitClass }.forEach { traits.remove(it) }

    /**
     * Checks if the trait container contains this trait class
     *
     * @param traitClass The class to check by for the array.
     *
     * @return Boolean value for if the list contains the following [traitClass]
     */
    fun hasTrait(traitClass: KClass<out T>): Boolean = traits.any { it::class == traitClass }

}

/**
 * Get a trait from the list from the class value
 *
 * @param traitClass The class to check by for the array.
 *
 * @return The trait
 */
inline fun <T : Trait, reified A : T> TraitContainer<T>.getTrait(): A = traits.first { it is A } as A
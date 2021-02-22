package world.cepi.itemextension.item.traits

import kotlin.reflect.KClass

/** Object that can hold Traits */
abstract class TraitContainer<T : ItemTrait> {

    /** List of traits that can be added or removed from. Please use a wrapper function */
    open val traits: MutableList<T> = mutableListOf()

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

    /**
     * Checks if the trait container contains any trait that is a subtype/is this class.
     *
     * @return Boolean value for if any traits are a subtype of this generic
     */
    inline fun <reified B : T> softHasTrait(): Boolean = traits.any { it is B }

    /** Removes all traits from the trait container. */
    fun removeAllTraits() = traits.removeAll { true }

    /**
     * Get a trait from the list from the class value
     *
     * @return The trait
     */
    inline fun <reified B : T> getTrait(): B? = traits.firstOrNull { (it as? B) != null } as? B

}
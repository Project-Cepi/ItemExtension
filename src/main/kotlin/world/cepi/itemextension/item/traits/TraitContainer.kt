package world.cepi.itemextension.item.traits

import kotlin.reflect.KClass

/** Object that can hold Traits */
abstract class TraitContainer<T : ItemTrait> {

    /** List of traits that can be added or removed from. Please use a wrapper function */
    open val traits: MutableMap<KClass<out T>, T> = mutableMapOf()

    /**
     * Encapsulation function to add a trait to the [traits] property
     *
     * @param traits The traits to add
     */
    fun put(vararg traits: T): TraitContainer<T> {
        traits.forEach { this.traits[it::class] = it }
        return this
    }

    operator fun T.unaryPlus() = put(this)

    /**
     * Safely removes a trait based on its class refrence.
     *
     * @param traitClass The class remove from the array.
     */
    fun removeTrait(traitClass: KClass<out T>) = traits.remove(traitClass)

    inline fun <reified B: T> removeTrait() = removeTrait(B::class)

    /**
     * Checks if the trait container contains this trait class
     *
     * @param traitClass The class to check by for the array.
     *
     * @return Boolean value for if the list contains the following [traitClass]
     */
    fun hasTrait(traitClass: KClass<out T>): Boolean = traits.containsKey(traitClass)

    inline fun <reified B: T> hasTrait(): Boolean = hasTrait(B::class)

    fun softHasTrait(clazz: KClass<out T>): Boolean = traits.values.any { clazz.isInstance(it) }

    /**
     * Checks if the trait container contains any trait that is a subtype/is this class.
     *
     * @return Boolean value for if any traits are a subtype of this generic
     */
    inline fun <reified B : T> softHasTrait(): Boolean = softHasTrait(B::class)

    fun softGet(clazz: KClass<out T>): List<T> = traits.values.filterIsInstance(clazz.java)

    inline fun <reified B: T> softGet(): List<B> = softGet(B::class) as List<B>

    /** Removes all traits from the trait container. */
    fun removeAllTraits() = traits.clear()

    fun get(clazz: KClass<out T>): T? = traits[clazz]

    /**
     * Get a trait from the list from the class value
     *
     * @return The trait
     */
    inline fun <reified B : T> get(): B? =
        this.get(B::class) as? B

}
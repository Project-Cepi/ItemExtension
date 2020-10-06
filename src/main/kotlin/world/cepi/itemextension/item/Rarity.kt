package world.cepi.itemextension.item

/**
 * Rarity enum for handling item rarities.
 */
enum class Rarity(
    /**
     * Number that identifies the Rarity for future-proofing items.
     */
    val number: Int,

    /**
     * The display name of the Rarity to be rendered on the item
     */
    val displayName: String
) {
}
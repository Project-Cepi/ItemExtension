package world.cepi.itemextension.data

import kotlinx.serialization.Serializable
import world.cepi.itemextension.item.Item

@Serializable
data class RegisteredItem(val key: String, val item: Item)
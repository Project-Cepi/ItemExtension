package world.cepi.itemextension.data

import world.cepi.kepi.data.DataNamespace
import world.cepi.kepi.data.model.JsonModel

object RegisteredItemModel : JsonModel<RegisteredItem>(
    RegisteredItem.serializer(),
    DataNamespace("item", "item"),
    { it.key }
)
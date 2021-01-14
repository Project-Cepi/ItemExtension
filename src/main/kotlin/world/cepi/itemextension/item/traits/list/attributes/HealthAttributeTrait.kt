package world.cepi.itemextension.item.traits.list.attributes

import world.cepi.itemextension.item.traits.list.AttributeTrait

class HealthAttributeTrait(override val value: Int): AttributeTrait() {

    override val name = "Health"

    override val taskIndex = 1
    override val loreIndex = 1


}
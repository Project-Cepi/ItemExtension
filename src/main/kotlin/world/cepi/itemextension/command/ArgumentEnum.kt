package world.cepi.itemextension.command

import net.minestom.server.command.builder.arguments.ArgumentWord

/** ArgumentEnum that can hold a list of Enums. Meant to transfer the properties of other enum data towards a handler. */
class ArgumentEnum(id: String) : ArgumentWord(id) {

    /** Internal enum array meant to represent the from restrictions. */
    var enumArray: Array<Enum<*>> = arrayOf()

    override fun from(vararg restrictions: String): ArgumentEnum {
        this.restrictions = restrictions
        return this
    }

}
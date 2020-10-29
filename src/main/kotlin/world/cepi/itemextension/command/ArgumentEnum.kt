package world.cepi.itemextension.command

import net.minestom.server.command.builder.arguments.ArgumentWord

class ArgumentEnum(id: String) : ArgumentWord(id) {

    var enumArray: Array<Enum<*>> = arrayOf()

    override fun from(vararg restrictions: String): ArgumentEnum {
        this.restrictions = restrictions
        return this
    }

}
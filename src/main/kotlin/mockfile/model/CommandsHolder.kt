package mockfile.model

import tornadofx.*

/**
 * @author d.khekk
@since 01.11.2017
 */
class CommandsHolder {

    var commandLines by property<String>()
    fun commandLinesProperty() = getProperty(CommandsHolder::commandLines)

    override fun toString(): String {
        return commandLines
    }
}

class CommandsModel : ItemViewModel<CommandsHolder>(CommandsHolder()) {
    val commandLines = bind { item?.commandLinesProperty() }
}
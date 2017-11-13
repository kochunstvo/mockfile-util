package mockfile.model

import tornadofx.*

/**
 * @author d.khekk
@since 01.11.2017
 */
class CommandsHolder {

    var commandLines by property<String>()
    var moreMessage by property(" ")
    fun commandLinesProperty() = getProperty(CommandsHolder::commandLines)
    fun moreMessageProperty() = getProperty(CommandsHolder::moreMessage)

    override fun toString(): String {
        return commandLines
    }
}

class CommandsModel : ItemViewModel<CommandsHolder>(CommandsHolder()) {
    val commandLines = bind { item?.commandLinesProperty() }
    val moreMessage = bind { item?.moreMessageProperty() }
}
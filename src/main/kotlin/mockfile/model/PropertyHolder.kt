package mockfile.model

import tornadofx.*

/**
 * @author d.khekk
@since 01.11.2017
 */
class PropertyHolder {

    var host by property("127.0.0.1")
    fun hostProperty() = getProperty(PropertyHolder::host)

    var port by property("23")
    fun portProperty() = getProperty(PropertyHolder::port)

    var login by property<String>()
    fun loginProperty() = getProperty(PropertyHolder::login)

    var pass by property<String>()
    fun passProperty() = getProperty(PropertyHolder::pass)

    var connectTimeout by property("5000")
    fun connectTimeoutProperty() = getProperty(PropertyHolder::connectTimeout)

    var commandTimeout by property("1000")
    fun commandTimeoutProperty() = getProperty(PropertyHolder::commandTimeout)

    override fun toString(): String {
        return "Хост: $host\nПорт: $port"
    }
}

class PropertyModel : ItemViewModel<PropertyHolder>(PropertyHolder()) {
    val host = bind { item?.hostProperty() }
    var port = bind { item?.portProperty() }
    var login = bind { item?.loginProperty() }
    var pass = bind { item?.passProperty() }
    var connectTimeout = bind { item?.connectTimeoutProperty() }
    var commandTimeout = bind { item?.commandTimeoutProperty() }
}
package mockfile.controller

import mockfile.telnet.TelnetResourceAskAction
import mockfile.model.CommandsHolder
import mockfile.model.PropertyHolder
import tornadofx.*

/**
 * @author d.khekk
@since 01.11.2017
 */
class GenProtocolController : Controller() {

    private val askAction = TelnetResourceAskAction()

    fun runController(properties: PropertyHolder, commandsHolder: CommandsHolder): String {
        val respTimeout = properties.commandTimeout.toLong()
        val connectTimeout = properties.connectTimeout.toLong()

        println("Started creating protocol file")

        askAction.openConnection(properties.host, properties.port.toInt())
        Thread.sleep(connectTimeout)

        val lines: List<String> = commandsHolder.commandLines.lines()

        val resultString = StringBuilder(askAction.readData() + "\n")

        val login = properties.login
        val pass = properties.pass

        println("Command: $login")
        askAction.send(login)
        Thread.sleep(respTimeout)
        var resp = askAction.readData()
        resultString.append("#cmd\n" +
                "$login\n" +
                "#rspns\n" +
                "$resp\n")
        println("Response: \n" + resp)

        println("Command: $pass")
        askAction.send(pass)
        Thread.sleep(respTimeout)
        resp = askAction.readData()
        resultString.append("#cmd\n" +
                "$pass\n" +
                "#rspns\n" +
                "$resp\n")
        println("Response: \n" + resp)

        val helloString = resp.split("\n").last().trim()

        lines.forEach {
            println("Execute command '$it'")
            askAction.send(it)
            Thread.sleep(respTimeout)
            resp = askAction.readData()
            resultString.append("#cmd\n" +
                    "$it\n" +
                    "#rspns\n" +
                    "$resp\n")
            println("Response: \n" + resp)

            while (!resp.contains(helloString)) {
                askAction.sendSpace()
                Thread.sleep(respTimeout)
                resp = askAction.readData()
                if (!resp.isBlank()) {
                    resultString.append("#more\n" +
                            "$resp\n")
                    println("More response: \n" + resp)
                }
            }
        }
        askAction.closeConnection()
        return resultString.toString().trim()
    }
}
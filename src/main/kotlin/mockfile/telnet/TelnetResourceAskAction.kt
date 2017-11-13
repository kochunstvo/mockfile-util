package mockfile.telnet

import org.apache.commons.net.telnet.EchoOptionHandler
import org.apache.commons.net.telnet.TelnetClient
import org.apache.commons.net.telnet.WindowSizeOptionHandler
import mockfile.write
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author d.khekk
@since 26.10.2017
 */
class TelnetResourceAskAction {
    private val terminalType = "vtnt"
    private val client: TelnetClient = TelnetClient(terminalType)

    init {
        client.addOptionHandler(EchoOptionHandler(false, true, true, true))
        client.addOptionHandler(WindowSizeOptionHandler(120, 1024, true, true, true, true))
    }

    fun openConnection(address: String, port: Int) {
        client.connect(address, port)
        if (!client.isConnected)
            throw IOException("Cannot connect to server")
    }

    fun closeConnection() {
        client.unregisterNotifHandler()
        client.disconnect()
    }

    fun readData(): String {
        val stream = client.inputStream
        val response = StringBuilder()

        val inputBufferSize = 255
        val inputBuffer = ByteArray(inputBufferSize)

        while (stream.available() != 0) {
            val readerCount = stream.read(inputBuffer, 0, inputBufferSize)
            val inputString = String(inputBuffer, 0, readerCount, Charset.forName("CP866"))
            response.append(inputString)
        }
        return response.toString()
    }

    fun send(request: String) {
        val outputStream = client.outputStream
        outputStream.write(request)
        outputStream.write(TelnetClient.NETASCII_EOL.toByteArray())
        outputStream.flush()
    }

    fun sendMore(message: String) {
        val outputStream = client.outputStream
        outputStream.write(message)
        outputStream.flush()
    }
}
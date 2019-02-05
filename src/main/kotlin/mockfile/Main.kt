package mockfile

import mockfile.views.MainView
import tornadofx.*
import java.io.OutputStream

/**
 * @author d.khekk

@since 26.10.2017
 */

class Main: App(MainView::class)

fun main(args: Array<String>) {
    launch<Main>(*args)
}

fun OutputStream.write(request: String) {
    this.write(request.toByteArray(java.nio.charset.Charset.forName("CP866")))
}
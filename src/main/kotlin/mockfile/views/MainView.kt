package mockfile.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.*
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.stage.FileChooser
import mockfile.controller.GenProtocolController
import mockfile.model.CommandsHolder
import mockfile.model.CommandsModel
import mockfile.model.PropertyModel
import tornadofx.*
import java.io.File
import java.io.FileWriter
import mockfile.model.PropertyHolder as Props

/**
 * @author d.khekk
@since 01.11.2017
 */
class MainView : View("Mockfile util") {
    private val propertyModel: PropertyModel by inject()
    private val commandsModel: CommandsModel by inject()
    private val controller: GenProtocolController by inject()

    private lateinit var property: Props
    private lateinit var commands: CommandsHolder
    private lateinit var content: String

    override val root = form {
        fieldset("Параметры подключения", FontAwesomeIconView(USER)) {
            field("Хост") {
                textfield(propertyModel.host).required()
            }
            field("Порт") {
                textfield(propertyModel.port).required()
            }
            field("Логин") {
                textfield(propertyModel.login).required()
            }
            field("Пароль") {
                textfield(propertyModel.pass).required()
            }
        }

        fieldset("Таймауты", FontAwesomeIconView(CLOCK_ALT)) {
            field("Ожидание после подключения") {
                textfield(propertyModel.connectTimeout).validator {
                    if (it != null && !it.matches(Regex("\\d+"))) {
                        error("Нужно ввести цифры")
                    } else null
                }
            }
            field("Ожидание ответа") {
                textfield(propertyModel.commandTimeout).validator {
                    if (it != null && !it.matches(Regex("\\d+"))) {
                        error("Нужно ввести цифры")
                    } else null
                }
            }
            field("Символ для продолжения ответа") {
                textfield(commandsModel.moreMessage)
            }
        }

        fieldset("Команды к оборудованию", FontAwesomeIconView(CLOUD_DOWNLOAD)) {
            field("Текст команд") {
                textarea(commandsModel.commandLines).required()
            }
        }

        button("Выполнить") {
            action {
                propertyModel.commit {
                    property = propertyModel.item
                }
                commandsModel.commit {
                    commands = commandsModel.item
                }
                runAsyncWithProgress {
                    content = controller.runController(property, commands)
                } success {
                    val fileChooser = FileChooser()
                    val filter = FileChooser.ExtensionFilter("Текстовые файлы (*.txt.out)", "*.txt.out")
                    fileChooser.extensionFilters += filter

                    var newFile = fileChooser.showSaveDialog(currentWindow)
                    if (newFile != null) {
                        saveFile(content, newFile)
                    }
                }
            }

            enableWhen(propertyModel.valid and commandsModel.valid)
        }
    }

    private fun saveFile(content: String, file: File) {
        val fileWriter = FileWriter(file)
        fileWriter.write(content)
        fileWriter.close()
    }
}

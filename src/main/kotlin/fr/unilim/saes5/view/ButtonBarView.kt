package fr.unilim.saes5.view

import fr.unilim.saes5.controller.ButtonBarController
import fr.unilim.saes5.model.Word
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import tornadofx.*
import java.io.File
import java.util.*

private const val bold_style = "-fx-font-weight: bold"

class ButtonBarView(
    private val myBundle: ResourceBundle,
    private var words: ObservableList<Word>,
    private val tokenInput: TextField,
    private val primaryContextInput: TextField,
    private val secondaryContextInput: TextField,
    private val synonymInput: TextField,
    private val antonymInput: TextField,
    private val definitionInput: TextArea,
    private val wordTableView: TableView<Word>? = null
) : View() {

    private var lastOpenedDirectory: File? = null
    private val defaultDirectory: File = File(System.getProperty("user.home"))
    private val buttonBarController = ButtonBarController(lastOpenedDirectory, defaultDirectory,null,words,myBundle)

    override val root = hbox(20.0) {
        paddingBottom = 20.0
        paddingHorizontal = 20.0
        alignment = Pos.BASELINE_RIGHT

        button(myBundle.getString("button_quit")) {
            addClass(ViewStyles.helpButton)
            action {
                Platform.exit()
            }
        }
        button(myBundle.getString("button_help")) {
            addClass(ViewStyles.helpButton)
            action {
                var dialog = Dialog<ButtonType>().apply {
                    initOwner(this@ButtonBarView.currentWindow)
                    title = myBundle.getString("help_title")
                    dialogPane.buttonTypes.add(ButtonType.CLOSE)
                    val textFlow = TextFlow(
                        Text(myBundle.getString("token_label") + "\n").apply { style = bold_style },
                        Text(myBundle.getString("statute_obligatory") + "\n" + myBundle.getString("description_token") + "\n\n"),
                        Text(myBundle.getString("definition_label") + "\n").apply {
                            style = bold_style
                        },
                        Text(myBundle.getString("statute_facultative") + "\n " + myBundle.getString("description_definition") + "\n\n"),
                        Text(myBundle.getString("primary_context_label") + "\n").apply {
                            style = bold_style
                        },
                        Text(myBundle.getString("statute_obligatory") + "\n" + myBundle.getString("description_primary_context") + "\n\n"),
                        Text(myBundle.getString("secondary_context_label") + "\n").apply {
                            style = bold_style
                        },
                        Text(myBundle.getString("statute_facultative") + "\n" + myBundle.getString("description_secondary_content") + "\n\n"),
                        Text(myBundle.getString("synonym_label") + "\n").apply { style = bold_style },
                        Text(myBundle.getString("statute_facultative") + "\n" + myBundle.getString("description_synonym") + "\n\n"),
                        Text(myBundle.getString("antonym_label") + "\n").apply { style = bold_style },
                        Text(myBundle.getString("statute_facultative") + "\n" + myBundle.getString("description_antonym") + "\n\n")
                    )
                    dialogPane.content = textFlow
                    dialogPane.prefWidth = primaryStage.width * 0.8
                    dialogPane.prefHeight = primaryStage.height * 0.5
                }
                val owner = this@ButtonBarView.currentWindow
                Platform.runLater {
                    dialog = buttonBarController.platformAction(owner,dialog)
                }
                dialog.showAndWait()
            }
        }
        button(myBundle.getString("button_download_file")) {
            addClass(ViewStyles.downloadButtonHover)
            action {
                lastOpenedDirectory = buttonBarController.downloadFile()
            }
        }
        button(myBundle.getString("button_download_folder")) {
            addClass(ViewStyles.downloadButtonHover)
            action {
                buttonBarController.downloadDirectory()
            }
        }
        button(myBundle.getString("button_add")) {
            addClass(ViewStyles.addButton)
            action {
                words = buttonBarController.addWordOrLaunchAlert(
                    tokenInput,
                    primaryContextInput,
                    definitionInput,
                    secondaryContextInput,
                    synonymInput,
                    antonymInput
                )
                wordTableView?.refresh()
            }
        }
    }
}

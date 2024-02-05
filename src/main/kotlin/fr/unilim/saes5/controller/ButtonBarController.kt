package fr.unilim.saes5.controller

import fr.unilim.saes5.model.Glossary
import fr.unilim.saes5.model.Word
import fr.unilim.saes5.model.context.PrimaryContext
import fr.unilim.saes5.model.context.SecondaryContext
import fr.unilim.saes5.model.reader.JavaFileReader
import fr.unilim.saes5.service.WordAnalyticsService
import fr.unilim.saes5.view.ViewUtilities
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Window
import tornadofx.alert
import java.io.File
import java.util.*


class ButtonBarController(
    private var lastOpenedDirectory  : File?,
    private val defaultDirectory:File,
    private var currentWindow : Window?,
    var words : ObservableList<Word>,
    private var myBundle : ResourceBundle) {

    fun platformAction(owner : Window?, dialog : Dialog<ButtonType>): Dialog<ButtonType> {
        if (owner != null) {
            val scene = owner.scene
            val x = owner.x + scene.x + (scene.width - dialog.dialogPane.width) / 2
            val y = owner.y + scene.y + (scene.height - dialog.dialogPane.height) / 2
            dialog.x = x
            dialog.y = y
        }
        return dialog
    }

    fun downloadFile(): File? {
        val fileChooser = FileChooser().apply {
            title = "Choisir des fichiers"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("Fichiers Java", "*.java"),
            )
            initialDirectory = lastOpenedDirectory ?: defaultDirectory
        }
        val selectedFiles = fileChooser.showOpenMultipleDialog(currentWindow)
        return if (selectedFiles != null) {
            val filePaths = selectedFiles.map { it.path }
            val analysisWords = JavaFileReader().read(filePaths)
            download(analysisWords)
            lastOpenedDirectory = selectedFiles.first().parentFile
            selectedFiles.first().parentFile
        }else{
            lastOpenedDirectory
        }
    }

    fun downloadDirectory(){
        val directoryChooser = DirectoryChooser().apply {
            title = "Choisir un dossier"
            initialDirectory = lastOpenedDirectory ?: defaultDirectory
        }
        directoryChooser.showDialog(currentWindow)?.let { file ->
            lastOpenedDirectory = file
            val analysisWords = JavaFileReader().read(file.toString())
            download(analysisWords)
        }
    }

    fun addWordOrLaunchAlert(tokenInput : TextField, primaryContextInput : TextField, definitionInput :TextArea, secondaryContextInput : TextField, synonymInput : TextField, antonymInput : TextField ): ObservableList<Word> {
        if (tokenInput.text.isBlank() || primaryContextInput.text.isBlank()) {
            alert(
                type = Alert.AlertType.WARNING,
                header = myBundle.getString("missing_fields_header"),
                content = myBundle.getString("missing_fields_content")
            )
        } else {
            addWord(tokenInput, primaryContextInput, definitionInput, secondaryContextInput, synonymInput, antonymInput)
        }
        return words
    }

    private fun addWord(tokenInput : TextField, primaryContextInput : TextField, definitionInput :TextArea, secondaryContextInput : TextField, synonymInput : TextField, antonymInput : TextField){
        val newWord = Word(tokenInput.text).apply {
            definition = definitionInput.text
            context = listOf(
                PrimaryContext(Word(primaryContextInput.text)),
                SecondaryContext(Word(secondaryContextInput.text))
            )
            synonyms = setOf(Word(synonymInput.text))
            antonyms = setOf(Word(antonymInput.text))
        }

        val duplicate = words.any { it.token == newWord.token }
        if (duplicate) {
            alert(
                type = Alert.AlertType.WARNING,
                header = myBundle.getString("duplicate_header"),
                content = myBundle.getString("duplicate_content")
            )
        } else {
            words.add(newWord)
            ViewUtilities.clearInputFields(
                tokenInput,
                primaryContextInput,
                secondaryContextInput,
                synonymInput,
                antonymInput,
                definitionInput
            )
            ViewUtilities.updateJsonFile(words)
            ViewUtilities.updateCompletionService()
        }
    }

    private fun download(analysisWords : List<Word>){
        val analytics = WordAnalyticsService()
        val wordRank = analytics.wordRank(analysisWords)
        val wordsInListNotInGlossary = analytics.wordsInListNotInGlossary(wordRank.keys.toList().map { it }, Glossary(words))
        val glossaryRatio = analytics.glossaryRatio(analysisWords, Glossary(words))
        ViewUtilities.openWordOccurrenceView(wordRank, wordsInListNotInGlossary, glossaryRatio, myBundle)
    }
}
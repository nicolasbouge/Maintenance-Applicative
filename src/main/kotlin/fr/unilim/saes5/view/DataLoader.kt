package fr.unilim.saes5.view

import fr.unilim.saes5.model.Word
import fr.unilim.saes5.persistence.project.JsonProjectDao
import fr.unilim.saes5.service.CompletionService
import javafx.collections.ObservableList

object DataLoader {
    fun loadSavedWords(words: ObservableList<Word>, contextCompletionService: CompletionService, lexicoCompletionService: CompletionService, tokenCompletionService: CompletionService) {
        val projectDao = JsonProjectDao("glossary.json")
        val projects = projectDao.allProject

        contextCompletionService.clearCompletions()
        lexicoCompletionService.clearCompletions()
        tokenCompletionService.clearCompletions()
        if (!projects.isNullOrEmpty()){
            projects.forEach { project ->
                project.words?.forEach { word ->
                    word.context?.forEach { context ->
                        contextCompletionService.addCompletion(context.word.token ?: "")
                    }
                    word.synonyms?.forEach { synonym ->
                        lexicoCompletionService.addCompletion(synonym.token ?: "")
                        tokenCompletionService.addCompletion(synonym.token ?: "")
                    }
                    word.antonyms?.forEach { antonym ->
                        lexicoCompletionService.addCompletion(antonym.token ?: "")
                        tokenCompletionService.addCompletion(antonym.token ?: "")
                    }
                    lexicoCompletionService.addCompletion(word.token ?: "")
                    if (!words.contains(word)) {
                        words.add(word)
                    }
                }
            }
        }

    }
}

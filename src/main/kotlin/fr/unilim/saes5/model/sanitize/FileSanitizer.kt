package fr.unilim.saes5.model.sanitize

import fr.unilim.saes5.model.Word

fun interface  FileSanitizer {
    fun sanitizeLines(lines: List<String>): List<Word>
}
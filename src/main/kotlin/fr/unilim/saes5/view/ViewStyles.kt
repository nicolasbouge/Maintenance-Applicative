package fr.unilim.saes5.view

import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

private const val white_color = "#ffffff"

private const val dm_sans_font = "DM Sans"


private const val sans_serif_font = "sans-serif"

private const val black_color = "#000000"

class ViewStyles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val addButton by cssclass()
        val helpButton by cssclass()
        val downloadButton by cssclass()
        val downloadButtonHover by cssclass()
        val customTextField by cssclass()
        val customTableView by cssclass()
        val customTableHeader by cssclass()
        val removeButton by cssclass()
    }

    init {
        heading {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        addButton {
            fontSize = 18.px
            cursor = Cursor.HAND
            backgroundColor += c("#84B71A")
            backgroundRadius += box(15.px)
            textFill = c(white_color)
            fontFamily = listOf(dm_sans_font, "Arial", "Helvetica", sans_serif_font).joinToString(",")

            and(hover) {
                backgroundColor += c("#388E3C")
            }
        }

        helpButton {
            fontSize = 18.px
            cursor = Cursor.HAND
            backgroundColor += c(white_color)
            backgroundRadius += box(15.px)
            borderRadius += box(15.px)
            borderWidth += box(1.px)
            borderColor += box(c(black_color))
            fontFamily = listOf(dm_sans_font, "Arial", "Helvetica", sans_serif_font).joinToString(",")

            and(hover) {
                backgroundColor += c("#D7D7D7")
            }
        }

        downloadButton {
            fontSize = 18.px
            cursor = Cursor.HAND
            backgroundColor += c(black_color)
            backgroundRadius += box(15.px)
            textFill = c(white_color)
            fontFamily = listOf(dm_sans_font, "Arial", "Helvetica", sans_serif_font).joinToString(",")

        }

        downloadButtonHover {

            fontSize = 18.px
            cursor = Cursor.HAND
            backgroundColor += c(black_color)
            backgroundRadius += box(15.px)
            textFill = c(white_color)
            fontFamily = listOf(dm_sans_font, "Arial", "Helvetica", sans_serif_font).joinToString(",")

            and(hover) {
                backgroundColor += c("#5C5E60")
            }
        }

        customTextField {
            borderWidth += box(1.px)
            borderColor += box(c(0, 0, 0, 1.0))
            backgroundRadius += box(15.px)
            borderRadius += box(15.px)
        }

        customTableView {
            backgroundColor += c("#E5E5E5")
            borderWidth += box(1.px)
            borderColor += box(c("#8C7E7E"))
            tabMaxWidth = 200.px
        }

        customTableHeader {
            textFill = c(white_color)
            backgroundColor += c("#736C6C")
        }


        removeButton {
            backgroundColor += Color.TRANSPARENT
            borderColor += box(Color.TRANSPARENT)
            textFill = Color.RED
            fontWeight = FontWeight.BOLD
            padding = box(0.px, 0.px, 0.px, 0.px)

            and(hover) {
                scaleX = 1.2
                scaleY = 1.2
            }
        }

        tableCell {
            alignment = Pos.CENTER
        }

        columnHeader {
            label {
                alignment = Pos.CENTER
            }
        }
    }


}

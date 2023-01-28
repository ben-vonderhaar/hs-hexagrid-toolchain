package com.hampstudios.toolchain

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame

class HexagridEditor : JFrame() {

    private val windowWidth = 1200
    private val windowHeight = 800

    init {
        title = "Hexagrid Editor"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(windowWidth, windowHeight)
        contentPane.background = Color.BLACK
        setLocationRelativeTo(null)


    }

    override fun paint(g: Graphics?) {
        super.paint(g)

        val boundingBoxPadding = 50 // pixels
        val gridPadding = 50 // pixels

        val xMin = boundingBoxPadding
        val yMin = boundingBoxPadding
        val xMax = windowWidth - (boundingBoxPadding * 2)
        val yMax = windowHeight - (boundingBoxPadding * 2)

        val nonOffsetRowPattern = listOf(true, true, false)
        val offsetRowPattern = listOf(true, false, true)

        g?.let {
            // Bounding box
            it.color = Color.WHITE
            it.drawRect(xMin, yMin, xMax, yMax)

            // Dots
            it.color = Color.CYAN
            var curX = xMin + gridPadding
            var curY = yMin + gridPadding
            var offsetRow = false
            var rowDrawMaskIndex = 0

            while (curY <= yMax) {

                val rowOffset = if (offsetRow) {
                    gridPadding / -2
                } else {
                    0
                }

                while (curX <= xMax) {
                    if (!offsetRow) {
                        if (nonOffsetRowPattern[rowDrawMaskIndex]) {
                            it.drawOval(curX - 2 + rowOffset, curY - 2, 4, 4)
                        }
                        rowDrawMaskIndex = (rowDrawMaskIndex + 1) % nonOffsetRowPattern.size
                    } else {
                        if (offsetRowPattern[rowDrawMaskIndex]) {
                            it.drawOval(curX - 2 + rowOffset, curY - 2, 4, 4)
                        }
                        rowDrawMaskIndex = (rowDrawMaskIndex + 1) % offsetRowPattern.size
                    }
                    curX += gridPadding // * (if (offsetRow) 2 else 1)
                }

                // 43.3 / 50 is a rough approximation of the proportion of
                // the height component of a hexagon to the width component
                curY += (gridPadding * (43.3 / 50)).toInt()
                curX = xMin + gridPadding
                offsetRow = !offsetRow
            }

            println(it.clipBounds)
        }
    }

}


fun main(args: Array<String>) {
    println("Hexagrid Editor args: $args")

    HexagridEditor().isVisible = true
}
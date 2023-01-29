package com.hampstudios.toolchain

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import javax.swing.JFrame
import kotlin.math.tan

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

        g?.let {
            // Bounding box
            it.color = Color.WHITE
            it.drawRect(xMin, yMin, xMax, yMax)

            // Dots
            it.color = Color.CYAN
            var curX = xMin + (gridPadding * 1.5).toInt()
            var curY = yMin + gridPadding
            var offsetRow = false

            while (curY <= yMax) {

                val rowOffset = if (offsetRow) {
                    gridPadding / -2
                } else {
                    0
                }

                while (curX <= xMax) {
                    it.drawOval(curX - 2 + rowOffset, curY - 2, 4, 4)
                    it.drawHexagonAtWithRadius(curX + rowOffset, curY, gridPadding)
                    curX += gridPadding // * (if (offsetRow) 2 else 1)
                }

                // TODO do not use this approximation
                curY += ((gridPadding / 2.0) * tan(Math.PI / 6)).toInt() * 3 - 2
                curX = xMin + (gridPadding * 1.5).toInt()
                offsetRow = !offsetRow
            }
        }
    }

}

private fun Graphics.drawHexagonAtWithRadius(x: Int, y: Int, width: Int) {


    val height = (width / 2.0) * tan(Math.PI / 6)

    val p0 = Point(x, y + (width / 2.0).toInt())
    val p1 = Point(x + (width / 2.0).toInt(), y + height.toInt())
    val p2 = Point(x + (width / 2.0).toInt(), y - height.toInt())
    val p3 = Point(x, y - (width / 2.0).toInt())
    val p4 = Point(x - (width / 2.0).toInt(), y - height.toInt())
    val p5 = Point(x - (width / 2.0).toInt(), y + height.toInt())

    this.drawLine(p0.x, p0.y, p1.x, p1.y)
    this.drawLine(p1.x, p1.y, p2.x, p2.y)
    this.drawLine(p2.x, p2.y, p3.x, p3.y)
    this.drawLine(p3.x, p3.y, p4.x, p4.y)
    this.drawLine(p4.x, p4.y, p5.x, p5.y)
    this.drawLine(p5.x, p5.y, p0.x, p0.y)

    //this.drawLine(x -5, y -5, x + 5, y + 5)
}


fun main(args: Array<String>) {
    println("Hexagrid Editor args: $args")

    HexagridEditor().isVisible = true
}
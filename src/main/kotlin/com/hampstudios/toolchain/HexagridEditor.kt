package com.hampstudios.toolchain

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.SystemColor.text
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import kotlin.math.floor
import kotlin.math.tan


class HexagridEditor : JFrame() {

    private val windowWidth = 1200
    private val windowHeight = 800

    //TODO make these modifiable
    private val gridPadding = 50 // pixels
    private val boundingBoxPadding = 50 // pixels
    private val xMin = boundingBoxPadding
    private val yMin = boundingBoxPadding
    private val xMax = windowWidth - (boundingBoxPadding * 2)
    private val yMax = windowHeight - (boundingBoxPadding * 2)

    private val hexagons = mutableSetOf<Hexagon>()

    init {
        title = "Hexagrid Editor"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(windowWidth, windowHeight)
        contentPane.background = Color.BLACK
        setLocationRelativeTo(null)

        addMouseMotionListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                super.mouseMoved(e)
            }

            override fun mouseMoved(e: MouseEvent) {
                super.mouseMoved(e)
            }
        })

        val gridHeight = floor((gridPadding / 2.0) * tan(Math.PI / 6)).toInt() + (gridPadding / 2.0).toInt()

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
                //it.drawOval(curX - 2 + rowOffset, curY - 2, 4, 4)
                createHexagonAtPointWithWidth(curX + rowOffset, curY, gridPadding)
                    .also { hex ->
                        //hex.draw(it)
                        hexagons.add(hex)
                    }
                curX += gridPadding
            }

            curY += gridHeight
            curX = xMin + (gridPadding * 1.5).toInt()
            offsetRow = !offsetRow
        }
    }

    override fun paint(g: Graphics?) {
        super.paint(g)

        g?.let { graphics ->
            // Bounding box
            graphics.color = Color.WHITE
            graphics.drawRect(xMin, yMin, xMax, yMax)

            // Dots
            graphics.color = Color.CYAN
            hexagons.forEach { it.draw(graphics) }
        }
    }

}

private fun createHexagonAtPointWithWidth(x: Int, y: Int, width: Int): Hexagon {
    val deltaY = floor((width / 2.0) * tan(Math.PI / 6)).toInt()

    return Hexagon(
        Point(x, y + (width / 2.0).toInt()),
        Point(x + (width / 2.0).toInt(), y + deltaY),
        Point(x + (width / 2.0).toInt(), y - deltaY),
        Point(x, y - (width / 2.0).toInt()),
        Point(x - (width / 2.0).toInt(), y - deltaY),
        Point(x - (width / 2.0).toInt(), y + deltaY)
    )
}

data class Hexagon(val p0: Point, val p1: Point, val p2: Point, val p3: Point, val p4: Point, val p5: Point) {
    fun draw(g: Graphics) {
        g.drawLine(p0.x, p0.y, p1.x, p1.y)
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
        g.drawLine(p2.x, p2.y, p3.x, p3.y)
        g.drawLine(p3.x, p3.y, p4.x, p4.y)
        g.drawLine(p4.x, p4.y, p5.x, p5.y)
        g.drawLine(p5.x, p5.y, p0.x, p0.y)
    }

    // TODO
    fun isWithin(x: Int, y: Int): Boolean {
        return false
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other || other !is Hexagon) {
            false
        } else {
            this.p0 == other.p0 &&
                this.p1 == other.p1 &&
                this.p2 == other.p2 &&
                this.p3 == other.p3 &&
                this.p4 == other.p4 &&
                this.p5 == other.p5
        }

    }
}

fun main(args: Array<String>) {
    println("Hexagrid Editor args: $args")

    HexagridEditor().isVisible = true
}
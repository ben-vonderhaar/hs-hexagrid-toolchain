package com.hampstudios.toolchain

import com.hampstudios.toolchain.HexagridEditor.Companion.windowHeight
import com.hampstudios.toolchain.HexagridEditor.Companion.windowWidth
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.math.floor
import kotlin.math.tan


class HexagridEditor : JPanel(), ActionListener, MouseAdapter {

    companion object {
        val windowWidth = 1200
        val windowHeight = 800
    }

    //TODO make these modifiable
    private val gridPadding = 50 // pixels
    private val boundingBoxPadding = 50 // pixels
    private val xMin = boundingBoxPadding
    private val yMin = boundingBoxPadding
    private val xMax = windowWidth - (boundingBoxPadding * 2)
    private val yMax = windowHeight - (boundingBoxPadding * 2)

    private val hexagons = mutableSetOf<Hexagon>()

    init {
        background = Color.BLACK

        addMouseListener(this)
        addMouseMotionListener(this)

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

    // Link frame ticks to panel paint
    override fun actionPerformed(p0: ActionEvent?) {
        this.repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        g?.let { graphics ->
            // Bounding box
            graphics.color = Color.WHITE
            graphics.drawRect(xMin, yMin, xMax, yMax)

            // Dots
            graphics.color = Color.CYAN
            hexagons.forEach { it.draw(graphics, this.mousePosition) }
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

/**
 * p0 is bottom of hexagon, and the remainder of points are provided CCW
 * TODO make this less cryptic
 */
data class Hexagon(val p0: Point, val p1: Point, val p2: Point, val p3: Point, val p4: Point, val p5: Point) {

    // Basically, a list of vectors
    private val segments: List<Pair<Int, Int>> = listOf(
        Pair(p1.x - p0.x, p1.y - p0.y),
        Pair(p2.x - p1.x, p2.y - p1.y),
        Pair(p3.x - p2.x, p3.y - p2.y),
        Pair(p4.x - p3.x, p4.y - p3.y),
        Pair(p5.x - p4.x, p5.y - p4.y),
        Pair(p0.x - p5.x, p0.y - p5.y)
    )

    private val center: Point = Point(p0.x, (p0.y + p3.y) / 2)

    fun draw(g: Graphics, mousePosition: Point?) {
        g.drawLine(p0.x, p0.y, p1.x, p1.y)
        g.drawLine(p1.x, p1.y, p2.x, p2.y)
        g.drawLine(p2.x, p2.y, p3.x, p3.y)
        g.drawLine(p3.x, p3.y, p4.x, p4.y)
        g.drawLine(p4.x, p4.y, p5.x, p5.y)
        g.drawLine(p5.x, p5.y, p0.x, p0.y)

        mousePosition?.let {
            if (isWithin(mousePosition.x, mousePosition.y)) {
                fill(g)
            }
        }
    }

    // TODO
    fun isWithin(x: Int, y: Int): Boolean {
        val midpoint = segments[0].midPoint()

        return x < p1.x
                && x > p4.x
                && y > p3.y
                && y < p0.y
        /*val pointToMidpoint = Vector(midpoint.x - x, midpoint.y - y)
        val crossZ = pointToMidpoint.first * segments[0].second - pointToMidpoint.second * segments[0].first

        return segments
            .map { Pair(it, it.midPoint().let { midpoint -> Vector(midpoint.x - x, midpoint.y - y) }) }
            .map { (segment, centerToSegment) -> centerToSegment.first * segment.second - centerToSegment.second * segment.first }
            .none { it < 0 }*/
    }

    fun fill(g: Graphics) {
        // Placeholder
        g.drawOval(center.x - 2, center.y - 2, 4, 4)

        // Scanline
        for (y in IntRange(p3.y, p0.y)) {

            // Find xMin and xMax for the given scanline

            // Can draw a simple rectangle for the middle of the hexagon
            if (y <= p1.y && y >= p2.y) {

                for (x in IntRange(p4.x, p2.x)) {
                    g.drawLine(x, y, x, y)
                }
            }

            // TODO genericize the below into "fill tri"

            // Draw top triangle
            if (y < p2.y) {
                // Find x extents for this scanline

                // Scale tri height
                val triScale: Double = (y - p3.y).toDouble() / (p2.y - p3.y).toDouble()

                // Tri from p3, [p3.x, p2.y], p2
                val maxX = p3.x + (triScale * (p2.x - p3.x))

                // Tri from p3, p4, [p3.x, p4.y]
                // TODO use proper points
                val minX = p3.x - (triScale * (p2.x - p3.x))

                for (x in IntRange(minX.toInt(), maxX.toInt())) {
                    g.drawLine(x, y, x, y)
                }
            }

            // Draw bottom triangle
            if (y > p1.y) {
                // Find x extents for this scanline
                // Scale tri height
                val triScale: Double = (p0.y - y).toDouble() / (p0.y - p1.y).toDouble()

                // Tri from p3, [p3.x, p2.y], p2
                val maxX = p0.x + (triScale * (p2.x - p3.x))

                // Tri from p3, p4, [p3.x, p4.y]
                // TODO use proper points
                val minX = p0.x - (triScale * (p2.x - p3.x))

                for (x in IntRange(minX.toInt(), maxX.toInt())) {
                    g.drawLine(x, y, x, y)
                }

            }
        }
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

typealias Vector = Pair<Int, Int>

fun Vector.midPoint(): Point {
    return Point(this.first / 2, this.second / 2)
}

fun main(args: Array<String>) {
    println("Hexagrid Editor args: $args")

    val frame = JFrame()
    frame.contentPane = HexagridEditor()
    frame.isVisible = true
    frame.title = "Hexagrid Editor"
    frame.defaultCloseOperation = EXIT_ON_CLOSE
    frame.size = Dimension(windowWidth, windowHeight)
    frame.setLocationRelativeTo(null)

    Timer(1000 / 30, frame.contentPane as HexagridEditor).start()
}
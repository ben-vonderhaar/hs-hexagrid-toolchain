package com.hampstudios.toolchain

import java.awt.Dimension
import javax.swing.JFrame

class HexagridEditor : JFrame() {

    init {
        title = "Hexagrid Editor"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(400, 300)
        setLocationRelativeTo(null)
    }

}


fun main(args: Array<String>) {
    println("Hexagrid Editor args: $args")

    HexagridEditor().isVisible = true
}
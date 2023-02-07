package com.hampstudios.toolchain

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

interface MouseAdapter : MouseListener, MouseMotionListener {
    override fun mouseReleased(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mouseEntered(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mouseClicked(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mouseExited(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mousePressed(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mouseMoved(p0: MouseEvent?) {
        // Do nothing by default
    }

    override fun mouseDragged(p0: MouseEvent?) {
        // Do nothing by default
    }

}
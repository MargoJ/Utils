package pl.margoj.utils.javafx.utils

import javafx.scene.canvas.Canvas

class ResizeableCanvas : Canvas()
{
    override fun isResizable(): Boolean = true

    override fun resize(width: Double, height: Double)
    {
        this.width = width
        this.height = height
    }

    override fun minWidth(height: Double): Double = 0.0

    override fun minHeight(width: Double): Double = 0.0

    override fun prefWidth(height: Double): Double = this.width

    override fun prefHeight(width: Double): Double = this.height
}
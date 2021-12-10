package hw.visualizer

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.paint.Color.RED
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class FXGraphVisualizer<T>(
    seed: Long = 0L,
    width: Double = 700.0,
    height: Double = 700.0,
    borderSize: Double = 20.0,
    pointRadius: Int = 5,
    fontSize: Int = 24
) : GraphVisualizer<T>(seed, width, height, borderSize, pointRadius, fontSize) {
    private val nodesToDraw: MutableList<Node> = mutableListOf()
    private val labelsToDraw: MutableList<Node> = mutableListOf()

    private fun drawArrow(from: Point, to: Point): Node {
        val phi = Math.toRadians(15.0)
        val barb = 20
        val dy = to.y - from.y
        val dx = to.x - from.x
        val theta = atan2(dy, dx)
        var rho = theta + phi

        val points = mutableListOf(to.x, to.y)

        repeat(2) {
            val x = to.x - barb * cos(rho)
            val y = to.y - barb * sin(rho)
            points += x
            points += y
            rho = theta - phi
        }
        return Polygon(*points.toDoubleArray())
    }

    override fun drawPoint(point: Point) {
        nodesToDraw += Circle(point.x, point.y, pointRadius.toDouble())
    }

    override fun drawLabel(point: Point) {
        labelsToDraw += Text(point.x, point.y, point.label).apply {
            fill = RED
            style = "-fx-font: $fontSize arial;"
        }
    }

    override fun drawLine(from: Point, to: Point) {
        nodesToDraw += Line(from.x, from.y, to.x, to.y)
        nodesToDraw += drawArrow(from, to)
    }

    override fun runVisualise() {
        Platform.startup { FXVisualizer(width, height, nodesToDraw, labelsToDraw).start(Stage()) }
    }
}

class FXVisualizer(
    private val width: Double,
    private val height: Double,
    private val nodesToDraw: MutableList<Node>,
    private val labelsToDraw: MutableList<Node>
) : Application() {
    override fun start(primaryStage: Stage) {
        val root = Group()
        root.children.addAll(nodesToDraw)
        root.children.addAll(labelsToDraw)
        primaryStage.scene = Scene(root, width, height)
        primaryStage.show()
    }
}
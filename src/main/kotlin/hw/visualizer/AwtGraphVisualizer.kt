package hw.visualizer

import java.awt.*
import java.awt.Frame.MAXIMIZED_BOTH
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Path2D
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

class AwtGraphVisualizer<T>(
    seed: Long = 0L,
    width: Double = 700.0,
    height: Double = 700.0,
    borderSize: Double = 50.0,
    pointRadius: Int = 10,
    fontSize: Int = 24
) : GraphVisualizer<T>(seed, width, height, borderSize, pointRadius, fontSize) {
    private val shapeActions: MutableList<(Graphics2D) -> Unit> = mutableListOf()
    private val textActions: MutableList<(Graphics2D) -> Unit> = mutableListOf()

    private fun drawArrow(g: Graphics2D, a: Point, b: Point) {
        val from = Point(normalizePoint(a.x), normalizePoint(a.y))
        val to = Point(normalizePoint(b.x), normalizePoint(b.y))
        g.draw(
            Line2D.Double(
                from.x,
                from.y,
                to.x,
                to.y,
            )
        )
        val phi = Math.toRadians(15.0)
        val barb = 20
        val dy = to.y - from.y
        val dx = to.x - from.x
        val theta = atan2(dy, dx)
        var rho = theta + phi

        val myPath: Path2D = Path2D.Double()
        myPath.moveTo(to.x, to.y)
        repeat(2) {
            val x = to.x - barb * cos(rho)
            val y = to.y - barb * sin(rho)
            myPath.lineTo(x, y)
            rho = theta - phi
        }
        myPath.closePath()
        g.fill(myPath)
    }

    private fun normalizePoint(x: Double): Double = x + pointRadius / 2.0

    override fun drawPoint(point: Point) {
        shapeActions += { graphics ->
            graphics.fill(Ellipse2D.Double(point.x, point.y, pointRadius.toDouble(), pointRadius.toDouble()))
        }
    }

    override fun drawLabel(point: Point) {
        textActions += { graphics ->
            graphics.drawString(point.label, point.x.toFloat(), point.y.toFloat())
        }
    }

    override fun drawLine(from: Point, to: Point) {
        shapeActions += { graphics -> drawArrow(graphics, from, to) }
    }

    override fun runVisualise() {
        val awtVisualizer = AwtVisualizer(shapeActions, textActions, fontSize, width, height)

        awtVisualizer.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })
        awtVisualizer.isVisible = true
        awtVisualizer.extendedState = MAXIMIZED_BOTH
    }
}

class AwtVisualizer(
    private val shapeActions: MutableList<(Graphics2D) -> Unit>,
    private val textActions: MutableList<(Graphics2D) -> Unit>,
    private val fontSize: Int,
    private val width: Double,
    private val height: Double
) : Frame() {
    override fun paint(graphics: Graphics) {
        val g2 = graphics as Graphics2D
        setSize(width.toInt(), height.toInt())

        g2.paint = Color.black
        shapeActions.forEach { it(g2) }

        g2.font = Font("Arial", Font.PLAIN, fontSize)
        g2.paint = Color.red
        textActions.forEach { it(g2) }
    }
}
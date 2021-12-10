package hw.visualizer

import hw.graph.Graph
import kotlin.random.Random

abstract class GraphVisualizer<T>(
    private val seed: Long,
    protected val width: Double,
    protected val height: Double,
    private val borderSize: Double,
    protected val pointRadius: Int,
    protected val fontSize: Int,
) {
    data class Point(
        val x: Double,
        val y: Double,
        val label: String = ""
    )

    protected abstract fun drawPoint(point: Point)
    protected abstract fun drawLabel(point: Point)
    protected abstract fun drawLine(from: Point, to: Point)

    protected abstract fun runVisualise()

    private fun drawPointWithLabel(point: Point) {
        drawPoint(point)
        drawLabel(point)
    }

    fun visualize(graph: Graph<T>) {
        val random = Random(seed)

        val points = graph.getNodes().map {
            Point(
                random.nextDouble(borderSize, width - borderSize),
                random.nextDouble(borderSize, height - borderSize),
                it.toString()
            )
        }

        points.forEach(::drawPointWithLabel)

        graph.edges.forEachIndexed { from, mutableList ->
            mutableList.forEach {
                drawLine(points[from], points[it])
            }
        }

        runVisualise()
    }

}
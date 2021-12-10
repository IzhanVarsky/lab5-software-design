package hw.graph

import hw.visualizer.GraphVisualizer
import java.nio.file.Path
import kotlin.io.path.bufferedReader

abstract class Graph<T>(private val drawingApi: GraphVisualizer<T>) {
    private var ind: Int = 0
    private val nodeToId: MutableMap<T, Int> = mutableMapOf()
    val edges: MutableList<MutableList<Int>> = mutableListOf()

    fun getNodes(): List<T> = nodeToId.keys.toList()

    fun addEdge(from: T, to: T) {
        tryAddNode(from)
        tryAddNode(to)
        edges[nodeToId[from]!!].add(nodeToId[to]!!)
    }

    fun readGraph(path: Path) {
        path.bufferedReader().useLines(::readEdgesFromStrings)
    }

    fun drawGraph() {
        drawingApi.visualize(this)
    }

    private fun tryAddNode(value: T) {
        if (nodeToId.containsKey(value)) return
        nodeToId[value] = ind++
        edges.add(mutableListOf())
    }

    private fun readEdgesFromStrings(seq: Sequence<String>) {
        seq.forEachIndexed(::addEdgesFromLine)
    }

    protected abstract fun addEdgesFromLine(ind: Int, line: String)
}


package hw.graph

import hw.visualizer.GraphVisualizer

class AdjacencyMatrix(drawingApi: GraphVisualizer<Int>) : Graph<Int>(drawingApi) {
    override fun addEdgesFromLine(ind: Int, line: String) {
        val nums = line.split("""\s""".toRegex()).map(String::toInt)
        nums.forEachIndexed { to, cnt ->
            repeat(cnt) {
                addEdge(ind, to)
            }
        }
    }
}
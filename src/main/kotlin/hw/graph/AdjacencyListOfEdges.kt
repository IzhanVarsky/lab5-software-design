package hw.graph

import hw.visualizer.GraphVisualizer

class AdjacencyListOfEdges(drawingApi: GraphVisualizer<Int>) : Graph<Int>(drawingApi) {
    override fun addEdgesFromLine(ind: Int, line: String) {
        val split = line.split(""":\s""".toRegex())
        val from = split[0].toInt()
        val nums = split[1].split(""",\s""".toRegex()).map(String::toInt)
        for (to in nums) {
            addEdge(from, to)
        }
    }
}
package hw.graph

import hw.visualizer.GraphVisualizer

class ListOfEdges(drawingApi: GraphVisualizer<Int>) : Graph<Int>(drawingApi) {
    override fun addEdgesFromLine(ind: Int, line: String) {
        val nums = line.split("""\s""".toRegex()).map(String::toInt)
        addEdge(nums[0], nums[1])
    }
}
package hw

import com.apurebase.arkenv.Arkenv
import com.apurebase.arkenv.util.argument
import com.apurebase.arkenv.util.parse
import hw.graph.AdjacencyListOfEdges
import hw.graph.AdjacencyMatrix
import hw.graph.ListOfEdges
import hw.visualizer.AwtGraphVisualizer
import hw.visualizer.FXGraphVisualizer
import hw.visualizer.GraphVisualizer
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.exists

val seed: Long
    get() = Instant.now().epochSecond

class Parameters : Arkenv() {
    val vizName: String by argument("--vizName") {
        val possibleNames = listOf("AWT", "JAVAFX")
        description = "Visualizer Name"
        validate("Unknown visualizer name. Possibles are: $possibleNames") { s ->
            possibleNames.any { it.equals(s, ignoreCase = true) }
        }
    }

    val graphType: String by argument("--graphType") {
        val possibleNames = listOf("AdjMatrix", "AdjList", "List")
        description = "Type of graph to visualise"
        validate("Unknown graph type. Possibles are: $possibleNames") { s ->
            possibleNames.any { it.equals(s, ignoreCase = true) }
        }
    }

    val input: Path by argument("--input") {
        description = "File with graph"
        mapping = { Path.of(it) }
        validate("File does not exist") {
            it.exists()
        }
    }
}

fun main(args: Array<String>) {
    val mode = true
    if (mode) {
        val visualizerName = "fx"
        listOf(
//        "al" to "./src/main/resources/AdjacencyList.txt",
//            "matrix" to "./src/main/resources/Matrix.txt",
            "list" to "./src/main/resources/ListOfEdges.txt",
        ).forEach {
            vizGraph(it.first, Path.of(it.second), visualizerName)
        }
        return
    }
    val parameters = Parameters().parse(args)
    if (parameters.help) {
        println(parameters.toString())
        return
    }
    vizGraph(parameters.graphType, parameters.input, parameters.vizName)
}

private fun vizGraph(
    graphName: String,
    graphFile: Path,
    visualizerName: String
) {
    val visualizer: GraphVisualizer<Int> = when (visualizerName.uppercase()) {
        "FX", "JAVAFX" -> FXGraphVisualizer(seed)
        "AWT" -> AwtGraphVisualizer(seed)
        else -> throw IllegalArgumentException("No such visualizer by name ${visualizerName.uppercase()}")
    }

    val graph = when (graphName.uppercase()) {
        "ADJMATRIX", "MATRIX" -> AdjacencyMatrix(visualizer)
        "ADJLIST", "AL" -> AdjacencyListOfEdges(visualizer)
        "LIST" -> ListOfEdges(visualizer)
        else -> throw IllegalArgumentException("No such graph by name ${graphName.uppercase()}")
    }.apply { readGraph(graphFile) }

    graph.drawGraph()
}
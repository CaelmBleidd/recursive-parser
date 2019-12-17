import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.stream.Collectors

internal class TreeDrawer {
    private fun createIdLine(startNode: Node, id: Int): String {
        return "$id [label=\"${startNode.name}\"" +
                "${(if (startNode.children.isEmpty() || startNode.name.isEmpty()) ", color=red" else "")}];"
    }

    private fun getId(treeNode: Node?): Int {
        return System.identityHashCode(treeNode)
    }

    private fun getChildrenLine(treeNode: Node): String {
        return if (treeNode.children.isEmpty() || treeNode.name.isEmpty()) {
            ""
        } else treeNode.children.stream()
                .map { getId(it) }
                .map { it.toString() }
                .collect(Collectors.joining(", "))
    }

    private fun dfs(treeNode: Node): String {
        val id = getId(treeNode)
        val result = StringBuilder(createIdLine(treeNode, id))
        result.append(id).append(" -> ").append("{")
        if (treeNode.children.isNotEmpty() && treeNode.name.isNotEmpty()) {
            result.append(getChildrenLine(treeNode))
        }
        result.append("};")
        if (treeNode.children.isNotEmpty() && treeNode.name.isNotEmpty()) {
            result.append(System.lineSeparator())
            for (child in treeNode.children) {
                result.append(dfs(child!!))
                result.append(System.lineSeparator())
            }
        }
        return result.toString()
    }

    fun printTree(tree: Node): String {
        return dfs(tree)
    }

    fun printToFile(tree: Node?, name: String) {
        val file = File("$name.dot")
        file.delete()
        file.createNewFile()
        val writer = BufferedWriter(FileWriter("$name.dot"))
        writer.write("digraph " + name + "{" + System.lineSeparator() + printTree(tree!!) + System.lineSeparator() + "}")
        writer.close()
    }
}
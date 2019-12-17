import java.text.ParseException

fun main() {
    val text = "function parse(a, b, c: integer; d, e, t: real) : real;"
    val parser = Parser()
    val treeDrawer = TreeDrawer()


    try {
        val node = parser.parse(text)
        if (node != null) {
            print(node)
            treeDrawer.printToFile(node, "test")
        }
    } catch (e: ParseException) {
        println(e.message + " Offset: " + e.errorOffset)
    }
}

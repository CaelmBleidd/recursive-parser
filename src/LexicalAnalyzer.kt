import java.text.ParseException

class LexicalAnalyzer(private var text: String, var currentPosition: Int = -1) {
    var currentChar = ' ';
    lateinit var currentToken: Token

    val types = hashSetOf("byte", "shortint", "integer", "word", "longint", "real", "single", "double", "extended")

    init {
        nextChar()
        nextToken()
    }

    fun nextToken() {
        while (currentChar.isWhitespace()) {
            nextChar()
        }

        if (currentChar.isLetter() || currentChar == '_') {
            val name = StringBuilder()

            while (currentChar.isLetterOrDigit() || currentChar == '_') {
                name.append(currentChar)
                nextChar()
            }

            currentToken = Token.getTokenByName(name.toString())
            if (currentToken == Token.UNKNOWN) {

                currentToken = if (name.toString() in types) Token.TYPE else
                    if (name.toString() == "var") Token.VAR else Token.NAME
            }
        } else {
            currentToken = Token.getTokenByName(currentChar.toString())
            nextChar()
            if (currentToken == Token.UNKNOWN) {
                throw ParseException("Unexpected character $currentChar at position $currentPosition", currentPosition)
            }
        }
    }

    fun nextChar() {
        currentChar = if (currentPosition < text.length - 1) text[++currentPosition] else '$'
    }

}
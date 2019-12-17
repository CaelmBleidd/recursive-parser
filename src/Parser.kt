import java.text.ParseException

class Parser() {
    lateinit var lexicalAnalyzer: LexicalAnalyzer

    private fun checkToken(expected: Token) {
        if (expected != lexicalAnalyzer.currentToken) {
            throw ParseException("Token $expected expected. Got: ${lexicalAnalyzer.currentToken} on position " +
                    "${lexicalAnalyzer.currentPosition}",
                    lexicalAnalyzer.currentPosition)
        }
    }

    private fun unexpectedTokenError() {
        throw ParseException("Unexpected token ${lexicalAnalyzer.currentToken} found", lexicalAnalyzer.currentPosition)
    }

    private fun signature(): Node? {
        if (lexicalAnalyzer.currentToken == Token.PROCEDURE || lexicalAnalyzer.currentToken == Token.FUNCTION) {
            val firstToken = lexicalAnalyzer.currentToken

            lexicalAnalyzer.nextToken()
            checkToken(Token.NAME)

            lexicalAnalyzer.nextToken()
            checkToken(Token.LPAREN)

            lexicalAnalyzer.nextToken()
            val arguments = args()

            checkToken(Token.RPAREN)

            lexicalAnalyzer.nextToken()

            return if (firstToken == Token.FUNCTION) {
                checkToken(Token.COLON)

                lexicalAnalyzer.nextToken()
                checkToken(Token.TYPE)

                lexicalAnalyzer.nextToken()
                checkToken(Token.SEMICOLON)

                lexicalAnalyzer.nextToken()
                checkToken(Token.END)

                Node("Signature", arrayListOf(Node("procedure"), Node("name"),
                        Node("("), arguments, Node(")"), Node(":"), Node("type"),
                        Node(";")))

            } else {
                checkToken(Token.SEMICOLON)

                lexicalAnalyzer.nextToken()
                checkToken(Token.END)
                Node("Signature", arrayListOf(Node("procedure"), Node("name"),
                        Node("("), arguments, Node(")"), Node(";")))
            }

        } else {
            unexpectedTokenError()
            return null
        }
    }

    private fun args(): Node? {
        return when (lexicalAnalyzer.currentToken) {
            Token.RPAREN -> Node("Args", arrayListOf(Node("eps")))
            Token.NAME, Token.VAR -> {
                val containsVar = lexicalAnalyzer.currentToken == Token.VAR
                if (containsVar) {
                    lexicalAnalyzer.nextToken()
                    checkToken(Token.NAME)
                }

                val argumentNames = names()

                checkToken(Token.COLON)

                lexicalAnalyzer.nextToken()
                checkToken(Token.TYPE)

                lexicalAnalyzer.nextToken()
                val argumentsPrimeNames = argsPrime()

                checkToken(Token.RPAREN)
                if (containsVar) {
                    Node("Args", arrayListOf(Node("var"), argumentNames, Node(":"), Node("type"), argumentsPrimeNames))
                } else {
                    Node("Args", arrayListOf(argumentNames, Node(":"), Node("type"), argumentsPrimeNames))
                }
            }
            else -> {
                unexpectedTokenError()
                null
            }
        }
    }

    private fun argsPrime(): Node? {
        return when (lexicalAnalyzer.currentToken) {
            Token.RPAREN -> Node("ArgsPrime", arrayListOf(Node("eps")))
            Token.SEMICOLON -> {
                lexicalAnalyzer.nextToken()

                val argumentNames = names()

                checkToken(Token.COLON)

                lexicalAnalyzer.nextToken()
                checkToken(Token.TYPE)

                lexicalAnalyzer.nextToken()
                val argumentPrime = argsPrime()

                Node("ArgsPrime", arrayListOf(Node(";"), argumentNames, Node(":"), Node("type"), argumentPrime))
            }
            else -> {
                unexpectedTokenError()
                null
            }
        }
    }

    private fun names(): Node? {
        return if (lexicalAnalyzer.currentToken == Token.NAME) {
            lexicalAnalyzer.nextToken()

            val namesPrime = namesPrime()
            Node("Names", arrayListOf(Node("name"), namesPrime))
        } else {
            unexpectedTokenError()
            null
        }
    }

    private fun namesPrime(): Node? {
        return when (lexicalAnalyzer.currentToken) {
            Token.COMMA -> {
                lexicalAnalyzer.nextToken()
                checkToken(Token.NAME)

                lexicalAnalyzer.nextToken()
                val namePrime = namesPrime()
                Node("NamePrime", arrayListOf(Node(","), Node("name"), namePrime))
            }
            Token.COLON -> {
                Node("NamePrime", arrayListOf(Node("eps")))
            }
            else -> {
                unexpectedTokenError()
                return null
            }
        }
    }

    fun parse(text: String): Node? {
        lexicalAnalyzer = LexicalAnalyzer(text)
        return signature()
    }
}
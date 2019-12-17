enum class Token(value: String) {
    FUNCTION("'function'"),
    PROCEDURE("'procedure'"),
    LPAREN("'('"),
    RPAREN("')"),
    COLON("':'"),
    COMMA("','"),
    SEMICOLON("';'"),
    NAME("'name'"),
    TYPE("'type'"),
    VAR("'var'"),
    END("$"),
    UNKNOWN("unknown");

    companion object {
        fun getTokenByName(name: String): Token {

            return when (name.toLowerCase()) {
                "function" -> FUNCTION
                "procedure" -> PROCEDURE
                "(" -> LPAREN
                ")" -> RPAREN
                ";" -> SEMICOLON
                ":" -> COLON
                "," -> COMMA
                "$" -> END
                else -> UNKNOWN
            }
        }
    }
}

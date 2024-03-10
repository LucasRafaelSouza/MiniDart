package lexical;
public enum TokenType {
    // SPECIALS
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,

    // SYMBOLS
    NULLABLE,     //?
    SEMICOLON,     // ;
    ASSIGN,        // =
    PARENTESES_L, //(
    PARENTESES_R, //)
    SQUARE_BRACKETS_L, //[
    SQUARE_BRACKETS_R, //]
    COMMA, //,
    CURLY_BRACKETS_L, //{
    CURLY_BRACKETS_R, //}
    TWO_POINTS, //:
    THREE_POINT,//...

    // OPERATORS
    ADD,           // +
    SUB,           // -
    MUL,           // *
    DIV,           // /
    MOD,           // %
    EQUAL,         // ==
    NOT_EQUAL,     // !=
    LOWER,         // <
    LOWER_EQUAL,   // <=
    GREATER,       // >
    GREATER_EQUAL, // >=
    AND, //&&
    OR, //||
    ELSE, // else
    IN, // in
    IF_NULL, //??
    DENIAL, //!
    DECREMENT,//--
    INCREMENT,//++
    IF, //if
    // KEYWORDS
    TERNARY, //?
    VAR,      //var,
    VARN,     //var?
    FINAL,   //final
    ASSERT, //assert
    PRINT,  //print
    WHILE, //while
    DO,    //do
    FOR,   //for
    READ, //read
    RANDOM, //random
    LENGTH, //length
    KEYS, //keys
    VALUES, //values
    TOBOOL, //tobool
    TOINT, //toint
    TOSTR, //torst
    // OTHERS
    FALSE,
    TRUE,
    NULL,
    NAME,          // identifier
    NUMBER,        // integer
    TEXT           // string

};
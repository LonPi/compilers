/*
  Created By: Fei Song
  File Name: tiny.flex
  To Build: jflex tiny.flex

  and then after the parser is created
    javac Lexer.java
*/

/* --------------------------Usercode Section------------------------ */

import java_cup.runtime.*;

%%

/* -----------------Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java.
*/
%class Lexer

%eofval{
  return null;
%eofval};

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column

/*
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup

/*
  Declarations

  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.
*/
%{
    /* To create a new java_cup.runtime.Symbol with information about
       the current token, the token will have no value in this
       case. */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    /* Also creates a new java_cup.runtime.Symbol with information
       about the current token, but this object has a value. */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}


/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */


/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */


/* A identifier integer is a word beginning a letter between A and
   Z, a and z, or an underscore followed by zero or more letters
   between A and Z, a and z, zero and nine, or an underscore. */
NUM = [0-9]+
ID = [_a-zA-Z][_a-zA-Z0-9]*
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = "/*"([^*]|[\r\n] | "*"+([^"*/"]|[\r\n]))*"*"+"/"
%%
/* ------------------------Lexical Rules Section---------------------- */

/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */

"if"               { return symbol(sym.IF, yytext()); }
"else"             { return symbol(sym.ELSE, yytext()); }
"while"            { return symbol(sym.WHILE, yytext()); }
"return"              { return symbol(sym.RETURN, yytext()); }
"int"             { return symbol(sym.INT, yytext()); }
"void"           { return symbol(sym.VOID, yytext()); }
"="               { return symbol(sym.ASSIGN, yytext()); }
"=="                { return symbol(sym.EQ, yytext()); }
"!="               { return symbol(sym.NEQ, yytext()); }
"<"                { return symbol(sym.LT, yytext()); }
">"                { return symbol(sym.GT, yytext()); }
"<="                { return symbol(sym.LTEQ, yytext()); }
">="                { return symbol(sym.GTEQ, yytext()); }
"+"                { return symbol(sym.PLUS, yytext()); }
"-"                { return symbol(sym.MINUS, yytext()); }
"*"                { return symbol(sym.TIMES, yytext()); }
"/"                { return symbol(sym.OVER, yytext()); }
"("                { return symbol(sym.LPAREN, yytext()); }
")"                { return symbol(sym.RPAREN, yytext()); }
"["                { return symbol(sym.LSQUARE, yytext()); }
"]"                { return symbol(sym.RSQUARE, yytext()); }
"{"                { return symbol(sym.LCURLY, yytext()); }
"}"                { return symbol(sym.RCURLY, yytext()); }
";"                { return symbol(sym.SEMI, yytext()); }
","                { return symbol(sym.COMMA, yytext()); }
{NUM}           { return symbol(sym.NUM, yytext()); }
{ID}       { return symbol(sym.ID, yytext()); }
{WhiteSpace}+      { /* skip whitespace */  }
{Comment}     { /* skip comments System.out.println(yytext());*/ }
.                  { return symbol(sym.ERROR, yytext()); }

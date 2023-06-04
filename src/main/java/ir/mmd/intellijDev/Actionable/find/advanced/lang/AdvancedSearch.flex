package ir.mmd.intellijDev.Actionable.find.advanced.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes;
import com.intellij.psi.TokenType;

%%

%class AdvancedSearchLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

IDENTIFIER=[a-z]([a-z\-]*[a-z])?
VARIABLE=\${IDENTIFIER}
SINGLE_QUOTE='
DOUBLE_QUOTE=\"
COLON=:
COMMA=,
LBRACE=\{
RBRACE=\}
EOS=\R|\;
LINE_COMMENT=\#[^\n]*
ESCAPED_CHAR=\\[^]
WHITESPACE=\s+
WHITESPACE_NO_CRLF=[ \t\f]+

%state MAIN
%state WHITESPACE
%state RAW_STRING
%state STRING

%%

<YYINITIAL> {
	{WHITESPACE} { yybegin(MAIN); return TokenType.WHITE_SPACE; }
    [^]          { yypushback(1); yybegin(MAIN); }
}

<MAIN> {
	{VARIABLE}           { return AdvancedSearchTypes.VARIABLE; }
	{IDENTIFIER}         { return AdvancedSearchTypes.IDENTIFIER; }
    {COLON}              { return AdvancedSearchTypes.COLON; }
    {COMMA}              { return AdvancedSearchTypes.COMMA; }
    {SINGLE_QUOTE}       { yybegin(RAW_STRING); return AdvancedSearchTypes.SINGLE_QUOTE; }
    {DOUBLE_QUOTE}       { yybegin(STRING); return AdvancedSearchTypes.DOUBLE_QUOTE; }
    {LBRACE}             { yybegin(WHITESPACE); return AdvancedSearchTypes.LBRACE; }
    {RBRACE}             { return AdvancedSearchTypes.RBRACE; }
    {EOS}                { yybegin(WHITESPACE); return AdvancedSearchTypes.EOS; }
    {WHITESPACE_NO_CRLF} { return TokenType.WHITE_SPACE; }
    {LINE_COMMENT}       { return AdvancedSearchTypes.COMMENT; }
}

<RAW_STRING> {
    {SINGLE_QUOTE} { yybegin(MAIN); return AdvancedSearchTypes.SINGLE_QUOTE; }
	[^']+          { return AdvancedSearchTypes.STRING_SEQ; }
}

<STRING> {
	{ESCAPED_CHAR} { return AdvancedSearchTypes.STRING_ESCAPE_SEQ; }
	{DOUBLE_QUOTE} { yybegin(MAIN); return AdvancedSearchTypes.DOUBLE_QUOTE; }
	[^\\\"]+       { return AdvancedSearchTypes.STRING_SEQ; }
}

<WHITESPACE> {
	{WHITESPACE} { yybegin(MAIN); return TokenType.WHITE_SPACE; }
    [^]          { yybegin(MAIN); yypushback(1); }
}

[^] {
	/* IElementType elType;
	while ((elType = advance()) == TokenType.BAD_CHARACTER) continue; */
	return TokenType.BAD_CHARACTER;
}

package ir.mmd.intellijDev.Actionable.find.advanced.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes;
import com.intellij.psi.TokenType;

%%

%{
	private Integer NEXT_STATE = null;
    private void nextstate(Integer i) {
		NEXT_STATE = i;
    }
	private int consumenextstate() throws NullPointerException {
		int i = NEXT_STATE;
		NEXT_STATE = null;
		return i;
	}

	private void ignorewhitespace(int nextState) {
		yybegin(WHITESPACE);
		nextstate(nextState);
	}
	private void ignorewhitespace() {
		ignorewhitespace(yystate());
	}
%}

%class AdvancedSearchLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

IDENTIFIER=[a-z]([a-z\-]*[a-z])?
VARIABLE=\${IDENTIFIER}
VALUE=[a-zA-Z0-9_\-.$ ]+
COLON=:
COMMA=,
LBRACE=\{
RBRACE=\}
CRLF=\R
DOUBLE_CRLF_OR_MORE=\R\R+
WHITESPACE=\s+

%state STATEMENT
%state WHITESPACE
%state VARIABLE
%state IDENTIFIER
%state AFTER_COMMA
%state AFTER_COLON
%state AFTER_RBRACE

%%

<YYINITIAL> {
	{IDENTIFIER}          { return AdvancedSearchTypes.IDENTIFIER; }
    {COLON}               { yybegin(AFTER_COLON); return AdvancedSearchTypes.COLON; }
    {DOUBLE_CRLF_OR_MORE} { ignorewhitespace(VARIABLE); yypushback(yytext().length() - 1); return AdvancedSearchTypes.CRLF; }
    {CRLF}                { return AdvancedSearchTypes.CRLF; }
}

<AFTER_COLON> {
    {VALUE} { yybegin(YYINITIAL); return AdvancedSearchTypes.VALUE; }
    [^]     { yybegin(YYINITIAL); yypushback(1); }
}

<WHITESPACE> {
	{WHITESPACE} { yybegin(consumenextstate()); return TokenType.WHITE_SPACE; }
    [^]          { yybegin(consumenextstate()); yypushback(1); }
}

<VARIABLE> {
	{VARIABLE}   { ignorewhitespace(IDENTIFIER); return AdvancedSearchTypes.VARIABLE; }
    {RBRACE}     { yybegin(AFTER_RBRACE); return AdvancedSearchTypes.RBRACE; }
}

<IDENTIFIER> {
    {IDENTIFIER} { ignorewhitespace(STATEMENT); return AdvancedSearchTypes.IDENTIFIER; }
}

<STATEMENT> {
    {VALUE}       { return AdvancedSearchTypes.VALUE; }
    {COMMA}       { ignorewhitespace(); return AdvancedSearchTypes.COMMA; }
    {LBRACE}      { ignorewhitespace(VARIABLE); return AdvancedSearchTypes.LBRACE; }
    {RBRACE}      { yybegin(AFTER_RBRACE); return AdvancedSearchTypes.RBRACE; }
    {CRLF}        { ignorewhitespace(VARIABLE); return AdvancedSearchTypes.CRLF; }
}

<AFTER_RBRACE> {
	{CRLF}    { ignorewhitespace(VARIABLE); return AdvancedSearchTypes.CRLF; }
    [\f\t ]+  { return TokenType.WHITE_SPACE; }
    [^]       { yybegin(VARIABLE); yypushback(1); }
}

[^] {
	/* IElementType elType;
	while ((elType = advance()) == TokenType.BAD_CHARACTER) continue; */
	return TokenType.BAD_CHARACTER;
}

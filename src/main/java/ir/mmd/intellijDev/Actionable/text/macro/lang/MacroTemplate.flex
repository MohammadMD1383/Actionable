package ir.mmd.intellijDev.Actionable.text.macro.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes;
import com.intellij.psi.TokenType;

%%

%class MacroTemplateLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

DOLLAR=\$
PLACEHOLDER_NAME=[A-Z_]+
NUMBER=[0-9]+
ESCAPE=\\
ESCAPED_ESCAPE=\\\\
ESCAPED_DOLLAR=\\\$
ANY_TEXT=[^$\\]+
ANY_TEXT_AFTER_ESCAPE=[^][^$\\]*

%state AFTER_DOLLAR
%state AFTER_ESCAPE

%%

<YYINITIAL> {
	{DOLLAR}         { yybegin(AFTER_DOLLAR); return MacroTemplateTypes.DOLLAR; }
	{ESCAPED_ESCAPE} { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_ESCAPE; }
	{ESCAPED_DOLLAR} { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_DOLLAR; }
	{ESCAPE}         { yybegin(AFTER_ESCAPE); return MacroTemplateTypes.ESCAPE; }
	{ANY_TEXT}       { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }
}

<AFTER_DOLLAR> {
	{DOLLAR}           { yybegin(AFTER_DOLLAR); return MacroTemplateTypes.DOLLAR; }
	{PLACEHOLDER_NAME} { yybegin(YYINITIAL); return MacroTemplateTypes.PLACEHOLDER_NAME; }
	{NUMBER}           { yybegin(YYINITIAL); return MacroTemplateTypes.NUMBER; }
	{ANY_TEXT}         { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }
    [^]                { yypushback(1); yybegin(YYINITIAL); }
}

<AFTER_ESCAPE> {ANY_TEXT_AFTER_ESCAPE} { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

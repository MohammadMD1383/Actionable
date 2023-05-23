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
ESCAPE=\\
ESCAPED_ESCAPE=\\\\
ESCAPED_DOLLAR=\\\$
ANY_TEXT=[^$\\]+
ANY_TEXT_AFTER_ESCAPE=[^][^$\\]*

%state AFTER_DOLLAR
%state AFTER_ESCAPE

%%

<YYINITIAL>    {DOLLAR}                { yybegin(AFTER_DOLLAR); return MacroTemplateTypes.DOLLAR; }
<YYINITIAL>    {ESCAPED_ESCAPE}        { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_ESCAPE; }
<YYINITIAL>    {ESCAPED_DOLLAR}        { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_DOLLAR; }
<YYINITIAL>    {ESCAPE}                { yybegin(AFTER_ESCAPE); return MacroTemplateTypes.ESCAPE; }

<AFTER_DOLLAR> {DOLLAR}                { yybegin(AFTER_DOLLAR); return MacroTemplateTypes.DOLLAR; }
<AFTER_DOLLAR> {PLACEHOLDER_NAME}      { yybegin(YYINITIAL); return MacroTemplateTypes.PLACEHOLDER_NAME; }
<AFTER_DOLLAR> {ANY_TEXT}              { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

<AFTER_ESCAPE> {ANY_TEXT_AFTER_ESCAPE} { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

<YYINITIAL>    {ANY_TEXT}              { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

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

%state INSIDE_PLACEHOLDER
%state AFTER_ESCAPE

%%

<YYINITIAL> {DOLLAR} { yybegin(INSIDE_PLACEHOLDER); return MacroTemplateTypes.DOLLAR; }
<YYINITIAL> {ESCAPED_ESCAPE} { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_ESCAPE; }
<YYINITIAL> {ESCAPED_DOLLAR} { yybegin(YYINITIAL); return MacroTemplateTypes.ESCAPED_DOLLAR; }
<YYINITIAL> {ESCAPE} { yybegin(AFTER_ESCAPE); return MacroTemplateTypes.ESCAPE; }

<INSIDE_PLACEHOLDER> {PLACEHOLDER_NAME} { yybegin(INSIDE_PLACEHOLDER); return MacroTemplateTypes.PLACEHOLDER_NAME; }
<INSIDE_PLACEHOLDER> {DOLLAR} { yybegin(YYINITIAL); return MacroTemplateTypes.DOLLAR; }
<INSIDE_PLACEHOLDER> [^$\\]+ { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

<AFTER_ESCAPE> [^] { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

<YYINITIAL> [^$\\]+ { yybegin(YYINITIAL); return MacroTemplateTypes.ANY_TEXT; }

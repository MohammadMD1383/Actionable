// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*;
import static ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchParserUtilKt.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class AdvancedSearchParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return AdvancedSearchFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(RAW_STRING, STRING, STRING_LITERAL),
  };

  /* ********************************************************** */
  // TopLevelProperties? Statements?
  static boolean AdvancedSearchFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AdvancedSearchFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = AdvancedSearchFile_0(b, l + 1);
    r = r && AdvancedSearchFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TopLevelProperties?
  private static boolean AdvancedSearchFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AdvancedSearchFile_0")) return false;
    TopLevelProperties(b, l + 1);
    return true;
  }

  // Statements?
  private static boolean AdvancedSearchFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AdvancedSearchFile_1")) return false;
    Statements(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // StringLiteral
  public static boolean Parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameter")) return false;
    if (!nextTokenIs(b, "<parameter>", DOUBLE_QUOTE, SINGLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = StringLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Parameter (COMMA Parameter)*
  public static boolean Parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters")) return false;
    if (!nextTokenIs(b, "<parameters>", DOUBLE_QUOTE, SINGLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, "<parameters>");
    r = Parameter(b, l + 1);
    r = r && Parameters_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA Parameter)*
  private static boolean Parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Parameters_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Parameters_1", c)) break;
    }
    return true;
  }

  // COMMA Parameter
  private static boolean Parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && Parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SINGLE_QUOTE STRING_SEQ? SINGLE_QUOTE
  public static boolean RawString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RawString")) return false;
    if (!nextTokenIs(b, SINGLE_QUOTE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RAW_STRING, null);
    r = consumeToken(b, SINGLE_QUOTE);
    p = r; // pin = 1
    r = r && report_error_(b, RawString_1(b, l + 1));
    r = p && consumeToken(b, SINGLE_QUOTE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // STRING_SEQ?
  private static boolean RawString_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RawString_1")) return false;
    consumeToken(b, STRING_SEQ);
    return true;
  }

  /* ********************************************************** */
  // VARIABLE IDENTIFIER Parameters? StatementBody?
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = consumeTokens(b, 1, VARIABLE, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, Statement_2(b, l + 1));
    r = p && Statement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, AdvancedSearchParser::StatementRecover);
    return r || p;
  }

  // Parameters?
  private static boolean Statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement_2")) return false;
    Parameters(b, l + 1);
    return true;
  }

  // StatementBody?
  private static boolean Statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement_3")) return false;
    StatementBody(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACE Statements? RBRACE
  public static boolean StatementBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT_BODY, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, StatementBody_1(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Statements?
  private static boolean StatementBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementBody_1")) return false;
    Statements(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !eos
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eos(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (Statement eos)+
  public static boolean Statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statements")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Statements_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!Statements_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Statements", c)) break;
    }
    exit_section_(b, m, STATEMENTS, r);
    return r;
  }

  // Statement eos
  private static boolean Statements_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statements_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Statement(b, l + 1);
    r = r && eos(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DOUBLE_QUOTE (STRING_SEQ | STRING_ESCAPE_SEQ)* DOUBLE_QUOTE
  public static boolean String(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "String")) return false;
    if (!nextTokenIs(b, DOUBLE_QUOTE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRING, null);
    r = consumeToken(b, DOUBLE_QUOTE);
    p = r; // pin = 1
    r = r && report_error_(b, String_1(b, l + 1));
    r = p && consumeToken(b, DOUBLE_QUOTE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (STRING_SEQ | STRING_ESCAPE_SEQ)*
  private static boolean String_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "String_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!String_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "String_1", c)) break;
    }
    return true;
  }

  // STRING_SEQ | STRING_ESCAPE_SEQ
  private static boolean String_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "String_1_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_SEQ);
    if (!r) r = consumeToken(b, STRING_ESCAPE_SEQ);
    return r;
  }

  /* ********************************************************** */
  // RawString | String
  public static boolean StringLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringLiteral")) return false;
    if (!nextTokenIs(b, "<string literal>", DOUBLE_QUOTE, SINGLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STRING_LITERAL, "<string literal>");
    r = RawString(b, l + 1);
    if (!r) r = String(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (TopLevelProperty eos)+
  public static boolean TopLevelProperties(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelProperties")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TopLevelProperties_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!TopLevelProperties_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TopLevelProperties", c)) break;
    }
    exit_section_(b, m, TOP_LEVEL_PROPERTIES, r);
    return r;
  }

  // TopLevelProperty eos
  private static boolean TopLevelProperties_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelProperties_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TopLevelProperty(b, l + 1);
    r = r && eos(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON StringLiteral
  public static boolean TopLevelProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelProperty")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TOP_LEVEL_PROPERTY, "<top level property>");
    r = consumeTokens(b, 1, IDENTIFIER, COLON);
    p = r; // pin = 1
    r = r && StringLiteral(b, l + 1);
    exit_section_(b, l, m, r, p, AdvancedSearchParser::TopLevelPropertyRecover);
    return r || p;
  }

  /* ********************************************************** */
  // !eos
  static boolean TopLevelPropertyRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelPropertyRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eos(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EOS+ | <<eof>> | <<rBraceAsEOSInBody>>
  static boolean eos(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eos")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = eos_0(b, l + 1);
    if (!r) r = eof(b, l + 1);
    if (!r) r = rBraceAsEOSInBody(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EOS+
  private static boolean eos_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eos_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EOS);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, EOS)) break;
      if (!empty_element_parsed_guard_(b, "eos_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

}

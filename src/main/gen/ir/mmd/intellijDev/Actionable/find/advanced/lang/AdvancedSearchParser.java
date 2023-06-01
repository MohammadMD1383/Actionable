// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class AdvancedSearchParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
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

  /* ********************************************************** */
  // TopLevelProperties Statements?
  static boolean AdvancedSearchFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AdvancedSearchFile")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TopLevelProperties(b, l + 1);
    r = r && AdvancedSearchFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Statements?
  private static boolean AdvancedSearchFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AdvancedSearchFile_1")) return false;
    Statements(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // VALUE
  public static boolean Parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameter")) return false;
    if (!nextTokenIs(b, VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE);
    exit_section_(b, m, PARAMETER, r);
    return r;
  }

  /* ********************************************************** */
  // Parameter (COMMA Parameter)*
  public static boolean Parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Parameters")) return false;
    if (!nextTokenIs(b, VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Parameter(b, l + 1);
    r = r && Parameters_1(b, l + 1);
    exit_section_(b, m, PARAMETERS, r);
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
  // VARIABLE IDENTIFIER Parameters StatementBody?
  public static boolean Statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = consumeTokens(b, 1, VARIABLE, IDENTIFIER);
    p = r; // pin = 1
    r = r && report_error_(b, Parameters(b, l + 1));
    r = p && Statement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, AdvancedSearchParser::StatementRecover);
    return r || p;
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT_BODY, "<statement body>");
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, StatementBody_1(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, AdvancedSearchParser::StatementBodyRecover);
    return r || p;
  }

  // Statements?
  private static boolean StatementBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementBody_1")) return false;
    Statements(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !(RBRACE | semi)
  static boolean StatementBodyRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementBodyRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StatementBodyRecover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RBRACE | semi
  private static boolean StatementBodyRecover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementBodyRecover_0")) return false;
    boolean r;
    r = consumeToken(b, RBRACE);
    if (!r) r = semi(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !semi
  static boolean StatementRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StatementRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !semi(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (Statement semi)+
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

  // Statement semi
  private static boolean Statements_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Statements_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Statement(b, l + 1);
    r = r && semi(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (TopLevelProperty semi)+
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

  // TopLevelProperty semi
  private static boolean TopLevelProperties_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelProperties_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = TopLevelProperty(b, l + 1);
    r = r && semi(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON VALUE
  public static boolean TopLevelProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelProperty")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TOP_LEVEL_PROPERTY, "<top level property>");
    r = consumeTokens(b, 1, IDENTIFIER, COLON, VALUE);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, AdvancedSearchParser::TopLevelPropertyRecover);
    return r || p;
  }

  /* ********************************************************** */
  // !semi
  static boolean TopLevelPropertyRecover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TopLevelPropertyRecover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !semi(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CRLF | <<eof>>
  static boolean semi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semi")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CRLF);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}

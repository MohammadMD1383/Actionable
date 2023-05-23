// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.text.macro.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class MacroTemplateParser implements PsiParser, LightPsiParser {

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
    return MacroTemplateFile(b, l + 1);
  }

  /* ********************************************************** */
  // (Placeholder | Text)*
  static boolean MacroTemplateFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MacroTemplateFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!MacroTemplateFile_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "MacroTemplateFile", c)) break;
    }
    return true;
  }

  // Placeholder | Text
  private static boolean MacroTemplateFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MacroTemplateFile_0")) return false;
    boolean r;
    r = Placeholder(b, l + 1);
    if (!r) r = Text(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DOLLAR PLACEHOLDER_NAME DOLLAR
  public static boolean Placeholder(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Placeholder")) return false;
    if (!nextTokenIs(b, DOLLAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOLLAR, PLACEHOLDER_NAME, DOLLAR);
    exit_section_(b, m, PLACEHOLDER, r);
    return r;
  }

  /* ********************************************************** */
  // ANY_TEXT | DOLLAR | ESCAPE | ESCAPED_DOLLAR | ESCAPED_ESCAPE | PLACEHOLDER_NAME
  static boolean Text(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Text")) return false;
    boolean r;
    r = consumeToken(b, ANY_TEXT);
    if (!r) r = consumeToken(b, DOLLAR);
    if (!r) r = consumeToken(b, ESCAPE);
    if (!r) r = consumeToken(b, ESCAPED_DOLLAR);
    if (!r) r = consumeToken(b, ESCAPED_ESCAPE);
    if (!r) r = consumeToken(b, PLACEHOLDER_NAME);
    return r;
  }

}

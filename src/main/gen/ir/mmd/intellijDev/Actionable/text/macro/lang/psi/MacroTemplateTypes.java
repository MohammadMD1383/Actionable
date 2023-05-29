// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.text.macro.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.impl.*;

public interface MacroTemplateTypes {

  IElementType CARET_INDICATOR = new MacroTemplateElementType("CARET_INDICATOR");
  IElementType PLACEHOLDER = new MacroTemplateElementType("PLACEHOLDER");

  IElementType ANY_TEXT = new MacroTemplateTokenType("ANY_TEXT");
  IElementType DOLLAR = new MacroTemplateTokenType("DOLLAR");
  IElementType ESCAPE = new MacroTemplateTokenType("ESCAPE");
  IElementType ESCAPED_DOLLAR = new MacroTemplateTokenType("ESCAPED_DOLLAR");
  IElementType ESCAPED_ESCAPE = new MacroTemplateTokenType("ESCAPED_ESCAPE");
  IElementType NUMBER = new MacroTemplateTokenType("NUMBER");
  IElementType PLACEHOLDER_NAME = new MacroTemplateTokenType("PLACEHOLDER_NAME");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CARET_INDICATOR) {
        return new MacroTemplatePsiCaretIndicatorImpl(node);
      }
      else if (type == PLACEHOLDER) {
        return new MacroTemplatePsiPlaceholderImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

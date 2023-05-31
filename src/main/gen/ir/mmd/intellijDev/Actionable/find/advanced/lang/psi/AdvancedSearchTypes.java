// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl.*;

public interface AdvancedSearchTypes {

  IElementType PARAMETER = new AdvancedSearchElementType("PARAMETER");
  IElementType PARAMETERS = new AdvancedSearchElementType("PARAMETERS");
  IElementType STATEMENT = new AdvancedSearchElementType("STATEMENT");
  IElementType STATEMENTS = new AdvancedSearchElementType("STATEMENTS");
  IElementType STATEMENT_BODY = new AdvancedSearchElementType("STATEMENT_BODY");
  IElementType TOP_LEVEL_PROPERTIES = new AdvancedSearchElementType("TOP_LEVEL_PROPERTIES");
  IElementType TOP_LEVEL_PROPERTY = new AdvancedSearchElementType("TOP_LEVEL_PROPERTY");

  IElementType COLON = new AdvancedSearchTokenType("COLON");
  IElementType COMMA = new AdvancedSearchTokenType("COMMA");
  IElementType CRLF = new AdvancedSearchTokenType("CRLF");
  IElementType IDENTIFIER = new AdvancedSearchTokenType("IDENTIFIER");
  IElementType LBRACE = new AdvancedSearchTokenType("LBRACE");
  IElementType RBRACE = new AdvancedSearchTokenType("RBRACE");
  IElementType VALUE = new AdvancedSearchTokenType("VALUE");
  IElementType VARIABLE = new AdvancedSearchTokenType("VARIABLE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PARAMETER) {
        return new AdvancedSearchPsiParameterImpl(node);
      }
      else if (type == PARAMETERS) {
        return new AdvancedSearchPsiParametersImpl(node);
      }
      else if (type == STATEMENT) {
        return new AdvancedSearchPsiStatementImpl(node);
      }
      else if (type == STATEMENTS) {
        return new AdvancedSearchPsiStatementsImpl(node);
      }
      else if (type == STATEMENT_BODY) {
        return new AdvancedSearchPsiStatementBodyImpl(node);
      }
      else if (type == TOP_LEVEL_PROPERTIES) {
        return new AdvancedSearchPsiTopLevelPropertiesImpl(node);
      }
      else if (type == TOP_LEVEL_PROPERTY) {
        return new AdvancedSearchPsiTopLevelPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

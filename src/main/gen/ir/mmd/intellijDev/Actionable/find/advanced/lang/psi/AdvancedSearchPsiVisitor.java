// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;

public class AdvancedSearchPsiVisitor extends PsiElementVisitor {

  public void visitParameter(@NotNull AdvancedSearchPsiParameter o) {
    visitPsiElement(o);
  }

  public void visitParameters(@NotNull AdvancedSearchPsiParameters o) {
    visitPsiElement(o);
  }

  public void visitRawString(@NotNull AdvancedSearchPsiRawString o) {
    visitStringLiteral(o);
  }

  public void visitStatement(@NotNull AdvancedSearchPsiStatement o) {
    visitPsiElement(o);
  }

  public void visitStatementBody(@NotNull AdvancedSearchPsiStatementBody o) {
    visitPsiElement(o);
  }

  public void visitStatements(@NotNull AdvancedSearchPsiStatements o) {
    visitPsiElement(o);
  }

  public void visitString(@NotNull AdvancedSearchPsiString o) {
    visitStringLiteral(o);
  }

  public void visitStringLiteral(@NotNull AdvancedSearchPsiStringLiteral o) {
    visitPsiLanguageInjectionHost(o);
  }

  public void visitTopLevelProperties(@NotNull AdvancedSearchPsiTopLevelProperties o) {
    visitPsiElement(o);
  }

  public void visitTopLevelProperty(@NotNull AdvancedSearchPsiTopLevelProperty o) {
    visitPsiElement(o);
  }

  public void visitPsiLanguageInjectionHost(@NotNull PsiLanguageInjectionHost o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}

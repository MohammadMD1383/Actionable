// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.*;

public class AdvancedSearchPsiStatementsImpl extends ASTWrapperPsiElement implements AdvancedSearchPsiStatements {

  public AdvancedSearchPsiStatementsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitStatements(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AdvancedSearchPsiStatement> getStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AdvancedSearchPsiStatement.class);
  }

}

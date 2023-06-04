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

public class AdvancedSearchPsiStatementImpl extends ASTWrapperPsiElement implements AdvancedSearchPsiStatement {

  public AdvancedSearchPsiStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AdvancedSearchPsiStatementBody getStatementBody() {
    return findChildByClass(AdvancedSearchPsiStatementBody.class);
  }

  @Override
  @Nullable
  public PsiElement getPsiVariable() {
    return AdvancedSearchPsiImplUtilKt.getPsiVariable(this);
  }

  @Override
  @Nullable
  public PsiElement getPsiIdentifier() {
    return AdvancedSearchPsiImplUtilKt.getPsiIdentifier(this);
  }

  @Override
  @Nullable
  public AdvancedSearchPsiParameters getPsiParameters() {
    return AdvancedSearchPsiImplUtilKt.getPsiParameters(this);
  }

}

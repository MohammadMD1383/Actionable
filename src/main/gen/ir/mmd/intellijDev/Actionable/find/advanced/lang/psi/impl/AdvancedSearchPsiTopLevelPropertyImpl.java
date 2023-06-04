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

public class AdvancedSearchPsiTopLevelPropertyImpl extends ASTWrapperPsiElement implements AdvancedSearchPsiTopLevelProperty {

  public AdvancedSearchPsiTopLevelPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitTopLevelProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public AdvancedSearchPsiStringLiteral getStringLiteral() {
    return findChildByClass(AdvancedSearchPsiStringLiteral.class);
  }

  @Override
  @Nullable
  public PsiElement getColon() {
    return findChildByType(COLON);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return findNotNullChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public String getKey() {
    return AdvancedSearchPsiImplUtilKt.getKey(this);
  }

  @Override
  @Nullable
  public String getValue() {
    return AdvancedSearchPsiImplUtilKt.getValue(this);
  }

}

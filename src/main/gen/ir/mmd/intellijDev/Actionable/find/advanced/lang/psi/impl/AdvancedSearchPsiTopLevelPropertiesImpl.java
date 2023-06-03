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

public class AdvancedSearchPsiTopLevelPropertiesImpl extends ASTWrapperPsiElement implements AdvancedSearchPsiTopLevelProperties {

  public AdvancedSearchPsiTopLevelPropertiesImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitTopLevelProperties(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AdvancedSearchPsiTopLevelProperty> getTopLevelPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AdvancedSearchPsiTopLevelProperty.class);
  }

  @Override
  @Nullable
  public AdvancedSearchPsiTopLevelProperty getLanguageProperty() {
    return AdvancedSearchPsiImplUtilKt.getLanguageProperty(this);
  }

}

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
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;

public class AdvancedSearchPsiParameterImpl extends ASTWrapperPsiElement implements AdvancedSearchPsiParameter {

  public AdvancedSearchPsiParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitParameter(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getValue() {
    return findNotNullChildByType(VALUE);
  }

  @Override
  public boolean isValidHost() {
    return AdvancedSearchPsiImplUtil.isValidHost(this);
  }

  @Override
  @NotNull
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    return AdvancedSearchPsiImplUtil.updateText(this, text);
  }

  @Override
  @NotNull
  public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return AdvancedSearchPsiImplUtil.createLiteralTextEscaper(this);
  }

}

// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.text.macro.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ir.mmd.intellijDev.Actionable.text.macro.lang.psi.MacroTemplateTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ir.mmd.intellijDev.Actionable.text.macro.lang.psi.*;

public class MacroTemplatePsiCaretIndicatorImpl extends ASTWrapperPsiElement implements MacroTemplatePsiCaretIndicator {

  public MacroTemplatePsiCaretIndicatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MacroTemplatePsiVisitor visitor) {
    visitor.visitCaretIndicator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MacroTemplatePsiVisitor) accept((MacroTemplatePsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getNumber() {
    return findNotNullChildByType(NUMBER);
  }

  @Override
  public int getNumberInt() {
    return MacroTemplatePsiImplUtilKt.getNumberInt(this);
  }

}

// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchTypes.*;
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.*;

public class AdvancedSearchPsiRawStringImpl extends AdvancedSearchPsiStringLiteralImpl implements AdvancedSearchPsiRawString {

  public AdvancedSearchPsiRawStringImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AdvancedSearchPsiVisitor visitor) {
    visitor.visitRawString(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AdvancedSearchPsiVisitor) accept((AdvancedSearchPsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getStringSeq() {
    return findChildByType(STRING_SEQ);
  }

}

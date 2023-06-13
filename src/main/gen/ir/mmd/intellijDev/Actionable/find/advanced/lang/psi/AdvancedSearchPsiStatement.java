// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AdvancedSearchPsiStatement extends PsiElement {

  @Nullable
  AdvancedSearchPsiStatementBody getStatementBody();

  @Nullable
  PsiElement getPsiVariable();

  @Nullable
  PsiElement getPsiIdentifier();

  @Nullable
  AdvancedSearchPsiParameters getPsiParameters();

  @Nullable
  String getVariable();

  @Nullable
  String getIdentifier();

  @NotNull
  List<AdvancedSearchPsiParameter> getParameters();

  @Nullable
  AdvancedSearchPsiStatement getParentStatement();

}

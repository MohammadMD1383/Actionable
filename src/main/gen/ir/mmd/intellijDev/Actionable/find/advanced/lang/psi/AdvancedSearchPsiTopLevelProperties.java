// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AdvancedSearchPsiTopLevelProperties extends PsiElement {

  @NotNull
  List<AdvancedSearchPsiTopLevelProperty> getTopLevelPropertyList();

  @Nullable
  AdvancedSearchPsiTopLevelProperty getLanguagePsiProperty();

  @Nullable
  AdvancedSearchPsiTopLevelProperty findPsiPropertyByKey(@NotNull String key, boolean ignoreCase);

  @Nullable
  AdvancedSearchPsiTopLevelProperty findPsiPropertyByKey(@NotNull String key);

}

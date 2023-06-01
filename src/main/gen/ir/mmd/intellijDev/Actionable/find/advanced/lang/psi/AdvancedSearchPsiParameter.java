// This is a generated file. Not intended for manual editing.
package ir.mmd.intellijDev.Actionable.find.advanced.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.LiteralTextEscaper;

public interface AdvancedSearchPsiParameter extends PsiLanguageInjectionHost {

  @NotNull
  PsiElement getValue();

  boolean isValidHost();

  @NotNull
  PsiLanguageInjectionHost updateText(@NotNull String text);

  @NotNull
  LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper();

}

package ir.mmd.intellijDev.Actionable.lang.java

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.JavaCodeFragmentFactory
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.EditorTextField
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.ui.showCustomInputDialog
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.psiFile

class AddImportDialogAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
		val psiFacade = JavaPsiFacade.getInstance(project)
		val psiPackage = psiFacade.findPackage("")
		val codeFragment = JavaCodeFragmentFactory.getInstance(project).createReferenceCodeFragment("", psiPackage, true, true)
		val fragmentDocument = PsiDocumentManager.getInstance(project).getDocument(codeFragment)
		val text = showCustomInputDialog(
			project,
			"Add Import",
			EditorTextField(fragmentDocument, project, JavaFileType.INSTANCE)
		) { it.text }
		
		if (text != null) {
			val psiClass = psiFacade.findClass(text, GlobalSearchScope.allScope(project)) ?: return
			runWriteCommandAction {
				JavaCodeStyleManager.getInstance(project).addImport(e.psiFile as PsiJavaFile, psiClass)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor && hasPsiFile && psiFile.fileType is JavaFileType }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

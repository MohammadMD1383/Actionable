package ir.mmd.intellijDev.Actionable.extra.java

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE
import com.intellij.psi.JavaCodeFragmentFactory
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.EditorTextField
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.util.ext.enableIf
import ir.mmd.intellijDev.Actionable.util.ext.psiFile
import ir.mmd.intellijDev.Actionable.util.ext.runWriteCommandAction

class AddImportDialogAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
		val psiFacade = JavaPsiFacade.getInstance(project)
		val psiPackage = psiFacade.findPackage("")
		val codeFragment = JavaCodeFragmentFactory.getInstance(project).createReferenceCodeFragment("", psiPackage, true, true)
		val fragmentDocument = PsiDocumentManager.getInstance(project).getDocument(codeFragment)
		val editorTextField = EditorTextField(fragmentDocument, project, JavaFileType.INSTANCE)
		
		val result = DialogBuilder(project).apply {
			setCenterPanel(editorTextField)
			setTitle("Add Import")
			removeAllActions()
			addOkAction()
			addCancelAction()
		}.show()
		
		if (result == OK_EXIT_CODE) {
			val psiClass = psiFacade.findClass(editorTextField.text, GlobalSearchScope.allScope(project)) ?: return
			project.runWriteCommandAction {
				JavaCodeStyleManager.getInstance(project).addImport(e.psiFile as PsiJavaFile, psiClass)
			}
		}
	}
	
	override fun update(e: AnActionEvent) = e.enableIf { hasEditor && hasPsiFile && psiFile.fileType is JavaFileType }
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

package ir.mmd.intellijDev.Actionable.psi

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfTypes
import ir.mmd.intellijDev.Actionable.util.ext.*
import kotlin.reflect.KClass

abstract class PsiAtCaretAction<T : PsiElement>(private val klass: KClass<T>) : AnAction() {
	private var cachedElement: T? = null
	
	abstract fun perform(element: T)
	
	private fun findElement(editor: Editor, psiFile: PsiFile): T? {
		val caret = editor.caretModel.primaryCaret
		return psiFile.elementAt(caret)?.parentOfTypes(klass, withSelf = true)
	}
	
	override fun actionPerformed(e: AnActionEvent) {
		cachedElement?.let { cachedElement = findElement(e.editor, e.psiFile) }
		cachedElement?.let { e.project.runWriteCommandAction { perform(it) } }
	}
	
	override fun update(e: AnActionEvent) = e.enableIf {
		hasProject and hasEditor and findElement(editor, psiFile)?.also { cachedElement = it }.isNotNull
	}
}

package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.*

/**
 * This model contains required fields for actions that are lazy-loaded and performance/resource friendly
 *
 * @see AnActionEvent
 */
class LazyEventContext(val event: AnActionEvent) {
	val project: Project? by lazy { event.project }
	val editor: Editor by lazy { event.editor }
	val document: Document by lazy { editor.document }
	val caretModel: CaretModel by lazy { editor.caretModel }
	val allCarets: List<Caret> by lazy { caretModel.allCarets }
	val primaryCaret: Caret by lazy { caretModel.primaryCaret }
	val psiFile: PsiFile by lazy { event.psiFile }
}

/**
 * This kind of action will be triggered for each [com.intellij.openapi.editor.Caret]
 */
abstract class MultiCaretAction : AnAction() {
	/**
	 * This method will be called for each [Caret]
	 */
	context (LazyEventContext)
	abstract fun perform(caret: Caret)
	
	override fun actionPerformed(e: AnActionEvent) = LazyEventContext(e).run {
		allCarets.forEach { perform(it) }
	}
}

/**
 * Use this if you want to [initialize] something that should be accessible by all of the [perform] calls
 *
 * Also, there is a [finalize] method to help you do something at the end of all [perform] calls
 *
 * @see MultiCaretAction
 */
abstract class MultiCaretActionWithInitialization<T> : MultiCaretAction() {
	private var _data: T? = null
	protected val data: T & Any get() = _data!!
	
	context (LazyEventContext)
	abstract fun initialize(): T & Any
	
	context (LazyEventContext)
	open fun finalize() = Unit
	
	override fun actionPerformed(e: AnActionEvent) = LazyEventContext(e).run {
		_data = initialize()
		allCarets.forEach { perform(it) }
		finalize()
		_data = null
	}
}

/**
 * Runs only if there is one [Caret] in the [Editor]
 *
 * @param forceSingleCaret force to have single caret in [AnAction.update]
 */
abstract class SingleCaretAction(private val forceSingleCaret: Boolean = true) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = LazyEventContext(e).run {
		perform(primaryCaret)
	}
	
	/**
	 * This will only be called once and only for the primary caret
	 */
	context (LazyEventContext)
	abstract fun perform(caret: Caret)
	
	/**
	 * `hasEditorWith { caretCount == 1 }` will automatically be applied to this if [forceSingleCaret] is set to `true`
	 */
	context (AnActionEvent)
	abstract val actionEnabled: Boolean
	
	override fun update(e: AnActionEvent) = e.enableIf { actionEnabled and (!forceSingleCaret || hasEditorWith { caretCount == 1 }) }
}

package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
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
	val selectionModel: SelectionModel by lazy { editor.selectionModel }
	val allCarets: List<Caret> by lazy { caretModel.allCarets }
	val primaryCaret: Caret by lazy { caretModel.primaryCaret }
	val psiFile: PsiFile by lazy { event.psiFile }
	
	/**
	 * This can be used to do some stuff in the scope of [LazyEventContext]
	 */
	inline operator fun <T> invoke(block: LazyEventContext.() -> T) : T = block()
}

/**
 * This kind of action will be triggered for each [com.intellij.openapi.editor.Caret]
 */
abstract class MultiCaretAction : AnAction {
	constructor(name: String) : super(name)
	constructor() : super()
	
	/**
	 * This method will be called for each [Caret]
	 */
	context (LazyEventContext)
	abstract fun perform(caret: Caret)
	
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
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
abstract class MultiCaretActionWithInitialization<T> : MultiCaretAction {
	constructor(name: String) : super(name)
	constructor() : super()
	
	/**
	 * backing field for [data]
	 */
	private var _data: T? = null
	
	/**
	 * return value of [initialize] will be stored in this field per action execution
	 */
	protected val data: T & Any get() = _data!!
	
	/**
	 * This method will be called once before any [perform] calls
	 *
	 * Return value of this method will be stored in [data] field for later use
	 */
	context (LazyEventContext)
	abstract fun initialize(): T & Any
	
	/**
	 * This method will be executed once after all [perform] calls have finished
	 *
	 * This can be used to do cleanup ...
	 */
	context (LazyEventContext)
	open fun finalize() = Unit
	
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
		_data = initialize()
		allCarets.forEach { perform(it) }
		finalize()
		_data = null
	}
}

/**
 * Runs only if there is one [Caret] in the [Editor] **OR** Runs only for the primary caret
 *
 * @param forceSingleCaret force to have single caret in [AnAction.update]
 */
abstract class SingleCaretAction(private val forceSingleCaret: Boolean = true) : AnAction() {
	override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) {
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
	open val actionEnabled: Boolean
		get() = true
	
	override fun update(e: AnActionEvent) = e.enableIf { actionEnabled and (!forceSingleCaret || hasEditorWith { caretCount == 1 }) }
}

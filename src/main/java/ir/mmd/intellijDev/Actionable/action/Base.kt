package ir.mmd.intellijDev.Actionable.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsActions
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import ir.mmd.intellijDev.Actionable.util.ext.*
import java.util.function.Supplier
import javax.swing.Icon

/**
 * This model contains required fields for actions.
 *
 * Fields are lazy-loaded and performance/resource friendly.
 *
 * @see AnActionEvent
 */
@Suppress("MemberVisibilityCanBePrivate")
class LazyEventContext(val event: AnActionEvent) {
	private val _project: Project? by lazy { event.project }
	private val _editor: Editor? by lazy { event.editor }
	private val _psiFile: PsiFile? by lazy { event.psiFile }
	private val _virtualFile: VirtualFile? by lazy { event.virtualFile }
	
	val project: Project get() = _project!!
	val editor: Editor get() = _editor!!
	val psiFile: PsiFile get() = _psiFile!!
	val virtualFile: VirtualFile get() = _virtualFile!!
	
	val document: Document by lazy { editor.document }
	val caretModel: CaretModel by lazy { editor.caretModel }
	val selectionModel: SelectionModel by lazy { editor.selectionModel }
	val scrollingModel: ScrollingModel by lazy { editor.scrollingModel }
	val allCarets: List<Caret> by lazy { caretModel.allCarets }
	val primaryCaret: Caret by lazy { caretModel.primaryCaret }
	val caretCount: Int by lazy { caretModel.caretCount }
	
	val hasProject get() = _project != null
	val hasEditor get() = _editor != null
	val hasPsiFile get() = _psiFile != null
	val hasVirtualFile get() = _virtualFile != null
	
	/**
	 * This can be used to do some stuff in the scope of [LazyEventContext]
	 */
	inline operator fun <T> invoke(block: context(LazyEventContext) () -> T): T = block(this)
	
	/**
	 * Use this instead of [Project.runWriteCommandAction] to prevent [NullPointerException] when the project can be null.
	 *
	 * This method invokes [com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction] with the nullable version of the [project]
	 */
	fun runWriteCommandAction(action: () -> Unit) = _project.runWriteCommandAction(action)
}

/**
 * The base action for all actions in Actionable plugin.
 */
@Suppress("unused")
abstract class ActionBase : AnAction {
	constructor() : super()
	constructor(icon: Icon?) : super(icon)
	constructor(text: @NlsActions.ActionText String?) : super(text)
	constructor(dynamicText: Supplier<@NlsActions.ActionText String>) : super(dynamicText)
	constructor(text: @NlsActions.ActionText String?, description: @NlsActions.ActionDescription String?, icon: Icon?) : super(text, description, icon)
	constructor(dynamicText: Supplier<@NlsActions.ActionText String>, icon: Icon?) : super(dynamicText, icon)
	constructor(dynamicText: Supplier<@NlsActions.ActionText String>, dynamicDescription: Supplier<@NlsActions.ActionDescription String>, icon: Icon?) : super(dynamicText, dynamicDescription, icon)
	
	context (LazyEventContext)
	abstract fun performAction()
	
	/**
	 * `hasProject` is forced for all actions.
	 *
	 * Specify criteria other than that here.
	 */
	context (LazyEventContext)
	open fun isEnabled(): Boolean = true
	
	final override fun actionPerformed(e: AnActionEvent) = (LazyEventContext(e)) { performAction() }
	final override fun update(e: AnActionEvent) = e.enableIf { hasProject and isEnabled() }
	final override fun isDumbAware() = super.isDumbAware()
	override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

/**
 * This kind of action will be triggered for each [com.intellij.openapi.editor.Caret]
 */
abstract class MultiCaretAction : ActionBase {
	constructor() : super()
	constructor(name: String) : super(name)
	
	/**
	 * This method will be called for each [Caret]
	 */
	context (LazyEventContext)
	abstract fun perform(caret: Caret)
	
	context(LazyEventContext)
	override fun performAction() = allCarets.forEach { perform(it) }
}

/**
 * Use this if you want to [initialize] something that should be accessible by all of the [perform] calls
 *
 * Also, there is a [finalize] method to help you do something at the end of all [perform] calls
 *
 * @see MultiCaretAction
 */
abstract class MultiCaretActionWithInitialization<T> : MultiCaretAction() {
	/**
	 * Backing field for [data]
	 */
	private var _data: T? = null
	
	/**
	 * Return value of [initialize] will be stored in this field per action execution.
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
	 * This can be used to doing cleanup ...
	 */
	context (LazyEventContext)
	open fun finalize() = Unit
	
	context (LazyEventContext)
	override fun performAction() {
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
abstract class SingleCaretAction(private val forceSingleCaret: Boolean = true) : ActionBase() {
	context(LazyEventContext)
	override fun performAction() = perform(primaryCaret)
	
	/**
	 * This will only be called once and only for the primary caret
	 */
	context (LazyEventContext)
	abstract fun perform(caret: Caret)
	
	context(LazyEventContext)
	override fun isEnabled() = hasEditor and (!forceSingleCaret || caretCount == 1)
}

package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.Language
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ex.ToolWindowManagerEx
import com.intellij.pom.Navigatable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.OpenSourceUtil
import com.intellij.util.application
import com.intellij.util.ui.JBUI
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import ir.mmd.intellijDev.Actionable.util.wrap
import java.awt.Component
import java.util.concurrent.Callable
import javax.swing.*

abstract class AdvancedSearchAgent protected constructor(
	protected val project: Project,
	protected val file: AdvancedSearchFile
) {
	companion object {
		@JvmStatic
		fun createAgent(project: Project, file: AdvancedSearchFile): AdvancedSearchAgent {
			if (PsiTreeUtil.hasErrorElements(file)) {
				throw IllegalStateException("aas file has errors that must be corrected before processing: $file").wrap()
			}
			
			val language = file.properties?.languagePsiProperty?.value
				?: throw IllegalArgumentException("language property must be set in the aas file: $file").wrap()
			
			if (!Language.getRegisteredLanguages().any { it.id.equals(language, ignoreCase = true) }) {
				throw UnsupportedOperationException("no language registered with name `$language` in file: $file").wrap()
			}
			
			try {
				val factory = AdvancedSearchExtensionPoint.findExtensionFor(language)
					?: throw UnsupportedOperationException("no agent found for language `$language` in file: $file").wrap()
				
				return factory.createAgent(project, file)
			} catch (e: Exception) {
				throw e.wrap(false)
			}
		}
	}
	
	class SearchResult(
		val text: String,
		val description: String?,
		val reference: Navigatable,
		val icon: Icon?
	)
	
	class SearchModel(
		val properties: Map<String, String>,
		val statements: List<SearchStatement>
	)
	
	class SearchStatement(
		val variable: String?,
		val identifier: String?,
		val parameters: List<String>,
		val innerStatements: List<SearchStatement>?
	)
	
	protected val model: SearchModel by lazy {
		val properties = mutableMapOf<String, String>()
		file.properties?.topLevelPropertyList?.forEach {
			properties[it.key] = it.value!!
		}
		
		val statements = mutableListOf<SearchStatement>()
		file.statements?.statementList?.forEach {
			statements += buildSearchStatement(it)
		}
		
		SearchModel(properties, statements)
	}
	
	private fun buildSearchStatement(psiStatement: AdvancedSearchPsiStatement): SearchStatement {
		val variable = psiStatement.psiVariable?.text
		val identifier = psiStatement.psiIdentifier?.text
		val parameters = psiStatement.parameters.map { it.stringLiteral.content }
		val innerStatements = psiStatement.statementBody?.statements?.statementList?.map {
			buildSearchStatement(it)
		}
		
		return SearchStatement(variable, identifier, parameters, innerStatements)
	}
	
	protected open fun createRenderer(): ListCellRenderer<SearchResult> {
		return object : JBLabel(), ListCellRenderer<SearchResult> {
			override fun getListCellRendererComponent(list: JList<out SearchResult>, value: SearchResult, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
				border = JBUI.Borders.empty(2)
				text = "<html><body><b>${value.text}</b>"
				icon = value.icon
				iconTextGap = 10
				
				value.description?.let {
					text += "<br><i>${value.description}</i>"
				}
				
				return this
			}
		}
	}
	
	protected abstract fun search(progress: ProgressIndicator, addResult: (SearchResult) -> Unit)
	
	fun process(progress: ProgressIndicator) {
		val findWindow = ToolWindowManagerEx.getInstanceEx(project).getToolWindow("Find")!!
		val manager = findWindow.contentManager
		val factory = manager.factory
		val listModel = DefaultListModel<SearchResult>()
		val component = JBScrollPane(
			JBList(listModel).apply {
				cellRenderer = createRenderer()
				selectionMode = ListSelectionModel.SINGLE_SELECTION
				
				selectionModel.addListSelectionListener {
					if (!it.valueIsAdjusting && selectedIndex != -1) {
						OpenSourceUtil.navigate(model.getElementAt(selectedIndex).reference)
					}
				}
			}
		).apply {
			border = JBUI.Borders.empty(5)
		}
		
		application.invokeAndWait {
			val content = factory.createContent(
				component,
				"Search results for: ${file.virtualFile.nameWithoutExtension}",
				false
			)
			
			manager.addContent(content)
			manager.requestFocus(content, true)
		}
		
		ReadAction.nonBlocking(Callable {
			search(progress) {
				application.invokeLater {
					listModel.addElement(it)
				}
			}
		}).inSmartMode(project).executeSynchronously()
	}
}

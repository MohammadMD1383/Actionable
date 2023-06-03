package ir.mmd.intellijDev.Actionable.find.advanced.agent

import com.intellij.lang.Language
import com.intellij.openapi.extensions.ExtensionPointName
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
import com.intellij.util.xmlb.annotations.Attribute
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.find.advanced.lang.psi.AdvancedSearchPsiStatement
import java.awt.Component
import javax.swing.*

class AdvancedSearchAgentBean {
	@Attribute("language")
	lateinit var language: String
	
	@Attribute("implementationClass")
	lateinit var implementationClass: String
}

abstract class AdvancedSearchAgent protected constructor(
	protected val project: Project,
	protected val file: AdvancedSearchFile
) {
	companion object {
		// todo: make it throw instead of returning null
		fun createAgent(project: Project, file: AdvancedSearchFile): AdvancedSearchAgent? {
			if (PsiTreeUtil.hasErrorElements(file)) {
				return null
			}
			
			val language = file.properties?.languageProperty?.propertyValue ?: return null
			if (!Language.getRegisteredLanguages().any { it.id.equals(language, ignoreCase = true) }) {
				return null
			}
			
			val className = EP_NAME.extensionList.find {
				it.language.equals(language, ignoreCase = true)
			}?.implementationClass ?: return null
			
			val constructor = Class.forName(className).getConstructor(Project::class.java, AdvancedSearchFile::class.java) ?: return null
			return constructor.newInstance(project, file) as AdvancedSearchAgent
		}
		
		val EP_NAME = ExtensionPointName.create<AdvancedSearchAgentBean>("ir.mmd.intellijDev.Actionable.find.advanced.agent")
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
		val variable: String,
		val identifier: String,
		val parameters: List<String>,
		val innerStatements: List<SearchStatement>?
	)
	
	protected val model: SearchModel by lazy {
		val properties = mutableMapOf<String, String>()
		file.properties?.topLevelPropertyList?.forEach {
			properties[it.propertyKey] = it.propertyValue!!
		}
		
		val statements = mutableListOf<SearchStatement>()
		file.statements?.statementList?.forEach {
			statements += buildSearchStatement(it)
		}
		
		SearchModel(properties, statements)
	}
	
	private fun buildSearchStatement(psiStatement: AdvancedSearchPsiStatement): SearchStatement {
		val variable = psiStatement.variable.text
		val identifier = psiStatement.identifier!!.text
		val parameters = psiStatement.parameters!!.parameterList.map { it.stringLiteral.stringText }
		val innerStatements = psiStatement.statementBody?.statements?.statementList?.map {
			buildSearchStatement(it)
		}
		
		return SearchStatement(variable, identifier, parameters, innerStatements)
	}
	
	protected fun createRenderer(): ListCellRenderer<SearchResult> {
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
			manager.addContent(factory.createContent(
				component,
				"Search results for: ${file.virtualFile.nameWithoutExtension}",
				false
			))
		}
		
		application.runReadAction {
			search(progress) {
				application.invokeLater {
					listModel.addElement(it)
				}
			}
		}
	}
}

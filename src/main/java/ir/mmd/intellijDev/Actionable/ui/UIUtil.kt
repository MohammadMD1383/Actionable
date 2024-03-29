package ir.mmd.intellijDev.Actionable.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import javax.swing.JComponent
import javax.swing.text.JTextComponent

fun showInputDialog(
	project: Project?,
	title: String,
	component: JTextComponent = JBTextField()
): String? {
	val dialog = DialogBuilder(project).apply {
		setTitle(title)
		setCenterPanel(component)
		removeAllActions()
		addOkAction()
		addCancelAction()
	}
	
	return if (dialog.show() == DialogWrapper.OK_EXIT_CODE) component.text else null
}

fun showMultilineInputDialog(
	project: Project?,
	title: String,
	rows: Int = 0,
	columns: Int = 0
) = showInputDialog(project, title, JBTextArea(rows, columns))

fun <T : JComponent> showCustomInputDialog(
	project: Project?,
	title: String,
	component: T,
	getComponentText: (T) -> String
): String? {
	val dialog = DialogBuilder(project).apply {
		setTitle(title)
		setCenterPanel(component)
		removeAllActions()
		addOkAction()
		addCancelAction()
	}
	
	return if (dialog.show() == DialogWrapper.OK_EXIT_CODE) getComponentText(component) else null
}

fun showCustomDialog(
	project: Project?,
	title: String,
	createCenterPanel: () -> JComponent
) = DialogBuilder(project).apply {
	setTitle(title)
	setCenterPanel(createCenterPanel())
	removeAllActions()
	addOkAction()
	addCancelAction()
}.show()

fun showConfirm(
	project: Project?,
	title: String,
	text: String
) = showCustomDialog(project, title) { JBLabel(text) } == DialogWrapper.OK_EXIT_CODE

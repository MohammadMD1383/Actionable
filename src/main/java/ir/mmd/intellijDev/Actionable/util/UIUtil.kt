package ir.mmd.intellijDev.Actionable.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
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

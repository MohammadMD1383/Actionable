package ir.mmd.intellijDev.Actionable.find.advanced

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchAgent
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import javax.swing.JOptionPane

class SearchAction : ActionBase() {
	context (LazyEventContext)
	override fun performAction() {
		val agent = AdvancedSearchAgent.createAgent(project, psiFile as AdvancedSearchFile)
		if (agent == null) {
			JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		
		action("ActivateFindToolWindow")!!.actionPerformed(event)
		
		ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Advanced search", true) {
			override fun run(indicator: ProgressIndicator) = agent.process(indicator)
		})
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasPsiFile && psiFile is AdvancedSearchFile
}

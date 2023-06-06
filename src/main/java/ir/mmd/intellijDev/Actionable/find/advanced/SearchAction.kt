package ir.mmd.intellijDev.Actionable.find.advanced

import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchAgent
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.util.ext.backgroundTask
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
		
		backgroundTask(project, "Advanced search", true) {
			agent.process(it)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasPsiFile && psiFile is AdvancedSearchFile
}

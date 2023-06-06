package ir.mmd.intellijDev.Actionable.find.advanced

import ir.mmd.intellijDev.Actionable.action.ActionBase
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.find.advanced.agent.AdvancedSearchAgent
import ir.mmd.intellijDev.Actionable.find.advanced.lang.AdvancedSearchFile
import ir.mmd.intellijDev.Actionable.util.ExceptionWrapper
import ir.mmd.intellijDev.Actionable.util.ext.backgroundTask
import javax.swing.JOptionPane

class SearchAction : ActionBase() {
	context (LazyEventContext)
	override fun performAction() {
		try {
			val agent = AdvancedSearchAgent.createAgent(project, psiFile as AdvancedSearchFile)
			action("ActivateFindToolWindow")!!.actionPerformed(event)
			backgroundTask(project, "Advanced search", true) {
				agent.process(it)
			}
		} catch (wrapper: ExceptionWrapper) {
			wrapper.throwIfShouldNotBeCaught()
			JOptionPane.showMessageDialog(null, wrapper.original.message, "Error", JOptionPane.ERROR_MESSAGE)
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasPsiFile && psiFile is AdvancedSearchFile
}

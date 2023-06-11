package ir.mmd.intellijDev.Actionable.find.advanced.lang

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import ir.mmd.intellijDev.Actionable.action.action
import ir.mmd.intellijDev.Actionable.find.advanced.SearchAction

@Suppress("CompanionObjectInExtension")
class AdvancedSearchLineMarkerProvider : RunLineMarkerContributor() {
	companion object {
		private val criteria = variable()
			.insideFirstStatement()
			.withParent(statement()
				.withoutParentStatement())
	}
	
	override fun getInfo(element: PsiElement): Info? {
		if (criteria.accepts(element)) {
			return Info(AllIcons.Actions.Search, { "Search..." }, action(SearchAction::class.qualifiedName!!))
		}
		
		return null
	}
}

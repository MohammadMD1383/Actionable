package ir.mmd.intellijDev.Actionable.lang.kotlin

import com.intellij.openapi.editor.Caret
import com.intellij.psi.util.parentOfType
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.elementAtOrBefore
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.core.setType
import org.jetbrains.kotlin.idea.structuralsearch.resolveDeclType
import org.jetbrains.kotlin.idea.structuralsearch.resolveExprType
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isNothing
import org.jetbrains.kotlin.types.typeUtil.isUnit

class SwitchFunctionBodyExpressionStyleAction : PsiActionAtCaret(removeCarets = false, inWriteAction = true) {
	context (LazyEventContext)
	override fun createModel(caret: Caret): Model? {
		val element = psiFile.elementAtOrBefore(caret)?.parentOfType<KtNamedFunction>() ?: return null
		return Model(caret, element)
	}
	
	context (LazyEventContext)
	override fun doAction(model: Model) {
		val function = model.psiElement as KtNamedFunction
		val psiFactory = KtPsiFactory(project)
		
		if (function.hasBlockBody()) {
			val bodyBlockExpression = function.bodyBlockExpression!!
			val functionReturnType: KotlinType = function.resolveDeclType() ?: return
			
			val firstStatement = bodyBlockExpression.firstStatement
			val equal = psiFactory.createEQ()
			
			when (firstStatement) {
				null -> {
					function.addBefore(equal, bodyBlockExpression)
					bodyBlockExpression.replace(psiFactory.createExpression("Unit"))
				}
				
				is KtThrowExpression -> {
					function.addBefore(equal, bodyBlockExpression)
					bodyBlockExpression.replace(firstStatement)
				}
				
				is KtReturnExpression -> {
					function.addBefore(equal, bodyBlockExpression)
					bodyBlockExpression.replace(firstStatement.returnedExpression!!)
					function.typeReference?.delete()
					function.colon?.delete()
				}
				
				else -> {
					val type = firstStatement.resolveExprType() ?: return
					
					if (type.isUnit() && functionReturnType.isUnit()) {
						function.addBefore(equal, bodyBlockExpression)
						bodyBlockExpression.replace(firstStatement)
						function.typeReference?.delete()
						function.colon?.delete()
					}
				}
			}
		} else {
			val bodyExpression = function.bodyExpression!!
			val returnType = bodyExpression.resolveExprType() ?: return
			
			function.equalsToken!!.delete()
			
			if (returnType.isUnit()) {
				if (bodyExpression.text == "Unit") {
					bodyExpression.replace(psiFactory.createEmptyBody())
				} else {
					bodyExpression.replace(psiFactory.createSingleStatementBlock(bodyExpression))
					function.typeReference?.delete()
					function.colon?.delete()
				}
			} else if (returnType.isNothing()) {
				bodyExpression.replace(
					psiFactory.createSingleStatementBlock(bodyExpression)
				)
			} else {
				val returnExpr = psiFactory.createExpressionByPattern("return $0", bodyExpression)
				bodyExpression.replace(psiFactory.createSingleStatementBlock(returnExpr))
				function.setType(returnType)
			}
		}
	}
	
	context (LazyEventContext)
	override fun isEnabled() = hasEditor && psiFile.fileType is KotlinFileType
}

package ir.mmd.intellijDev.Actionable.caret.editing

import com.goide.psi.GoAddExpr
import com.goide.psi.GoAndExpr
import com.goide.psi.GoMulExpr
import com.goide.psi.GoOrExpr
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyadicExpression
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import ir.mmd.intellijDev.Actionable.action.LazyEventContext
import ir.mmd.intellijDev.Actionable.caret.PsiActionAtCaret
import ir.mmd.intellijDev.Actionable.util.ext.contains
import ir.mmd.intellijDev.Actionable.util.ext.copyToClipboard
import org.jetbrains.kotlin.psi.KtBinaryExpression

abstract class OperandAtCaretAction(inWriteAction: Boolean = false) : PsiActionAtCaret(inWriteAction) {
	context (LazyEventContext)
	abstract fun execute(operand: PsiElement)
	
	context(LazyEventContext)
	override fun doAction(model: Model) {
		when (psiFile.fileType.name.lowercase()) {
			"java" -> model.psiElement.parentOfType<PsiPolyadicExpression>()?.run {
				this.operands.forEach {
					if (model.psiElement in it) {
						return@run it
					}
				}
				
				null
			}
			
			"kotlin" -> model.psiElement.parentOfType<KtBinaryExpression>()?.run {
				if (left?.contains(model.psiElement) == true) left else right
			}
			
			"javascript",
			"typescript" -> model.psiElement.parentOfType<JSBinaryExpression>()?.run {
				if (lOperand?.contains(model.psiElement) == true) lOperand else rOperand
			}
			
			"go" -> model.psiElement.parentOfTypes(
				GoAddExpr::class,
				GoMulExpr::class,
				GoAndExpr::class,
				GoOrExpr::class,
			)?.run {
				if (model.psiElement in left) left else right
			}
			
			else -> null
		}?.let {
			execute(it)
		}
	}
}

class DeleteOperandAtCaretAction : OperandAtCaretAction(true) {
	context(LazyEventContext)
	override fun execute(operand: PsiElement) = operand.delete()
}

class CopyOperandAtCaretAction : OperandAtCaretAction() {
	context(LazyEventContext)
	override fun execute(operand: PsiElement) = operand.text.copyToClipboard()
}

class CutOperandAtCaretAction : OperandAtCaretAction(true) {
	context(LazyEventContext)
	override fun execute(operand: PsiElement) {
		operand.text.copyToClipboard()
		operand.delete()
	}
}

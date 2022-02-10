package ir.mmd.intellijDev.Actionable.selection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import ir.mmd.intellijDev.Actionable.caret.movement.CaretMovementUtil;
import ir.mmd.intellijDev.Actionable.caret.movement.settings.SettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ir.mmd.intellijDev.Actionable.util.Utility.safeGet;

public class WordBasedExtendSelection extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
		final Document document = editor.getDocument();
		final CharSequence documentChars = document.getCharsSequence();
		final String wordSeparators = SettingsState.getInstance().wordSeparators;
		final List<Caret> carets = editor.getCaretModel().getAllCarets();
		
		for (Caret caret : carets) {
			final CaretMovementUtil cutil = new CaretMovementUtil(document, caret);
			final int selectionStart = caret.getSelectionStart();
			final int selectionEnd = caret.getSelectionEnd();
			
			final Character[] startChars = {
				safeGet(documentChars, selectionStart - 1),
				safeGet(documentChars, selectionStart)
			};
			final Character[] endChars = {
				safeGet(documentChars, selectionEnd - 1),
				safeGet(documentChars, selectionEnd)
			};
			
			// if (startChars[0] == startChars[1]) {
			// 	cutil.setOffset(selectionStart);
			// 	moveCaret(
			// 		cutil,
			// 		wordSeparators, ,
			// 		STOP_AT_CHAR_TYPE_CHANGE,
			// 		BACKWARD);
			// }
			//
			// if (endChars[0] == endChars[1]) {
			// 	cutil.setOffset(selectionEnd);
			// 	moveCaret(
			// 		cutil,
			// 		wordSeparators, ,
			// 		STOP_AT_CHAR_TYPE_CHANGE,
			// 		FORWARD);
			// }
		}
	}
	
	@Override
	public void update(@NotNull AnActionEvent e) {
		final Project project = e.getProject();
		final Editor editor = e.getData(CommonDataKeys.EDITOR);
		
		e.getPresentation().setEnabled(
			project != null && editor != null
		);
	}
}

package ir.mmd.intellijDev.Actionable.selection;

// --Commented out by Inspection START (2/14/22, 1:42 PM):
// --Commented out by Inspection START (2/14/22, 1:42 PM):
////public class WordBasedExtendSelection extends AnAction {
////	@Override
////	public void actionPerformed(@NotNull AnActionEvent e) {
////		final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
////		final Document document = editor.getDocument();
////		final CharSequence documentChars = document.getCharsSequence();
////		final String wordSeparators = SettingsState.getInstance().wordSeparators;
////		final List<Caret> carets = editor.getCaretModel().getAllCarets();
////
////		for (Caret caret : carets) {
////			final CaretMovementUtil cutil = new CaretMovementUtil(document, caret);
////			final int selectionStart = caret.getSelectionStart();
////			final int selectionEnd = caret.getSelectionEnd();
////
////			final Character[] startChars = {
////				safeGet(documentChars, selectionStart - 1),
////				safeGet(documentChars, selectionStart)
////			};
////			final Character[] endChars = {
////				safeGet(documentChars, selectionEnd - 1),
////				safeGet(documentChars, selectionEnd)
////			};
////
////			// if (startChars[0] == startChars[1]) {
////			// 	cutil.setOffset(selectionStart);
////			// 	moveCaret(
////			// 		cutil,
////			// 		wordSeparators, ,
////			// 		STOP_AT_CHAR_TYPE_CHANGE,
////			// 		BACKWARD);
////			// }
////			//
////			// if (endChars[0] == endChars[1]) {
////			// 	cutil.setOffset(selectionEnd);
////			// 	moveCaret(
////			// 		cutil,
////			// 		wordSeparators, ,
////			// 		STOP_AT_CHAR_TYPE_CHANGE,
////			// 		FORWARD);
////			// }
////		}
////	}
////
////	@Override
////	public void update(@NotNull AnActionEvent e) {
////		final Project project = e.getProject();
// --Commented out by Inspection STOP (2/14/22, 1:42 PM)
//		final Editor editor = e.getData(CommonDataKeys.EDITOR);
//
//		e.getPresentation().setEnabled(
//			project != null && editor != null
//		);
//	}
//}
// --Commented out by Inspection STOP (2/14/22, 1:42 PM)

package ir.mmd.intellijDev.Actionable.scripting;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import ir.mimlang.jmim.lang.interpreter.Interpreter;
import ir.mimlang.jmim.lang.lexer.Lexer;
import ir.mimlang.jmim.lang.node.Node;
import ir.mimlang.jmim.lang.parser.Parser;
import ir.mimlang.jmim.lang.token.Token;
import ir.mmd.intellijDev.Actionable.scripting.util.ActionableContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * this class holds the implementation for CustomAction[1-9]
 */
public class Actions {
	/**
	 * executes mim code in a context of {@link com.intellij.openapi.actionSystem.AnAction}
	 *
	 * @param e      instance of {@link AnActionEvent}
	 * @param code   the mim code to execute
	 * @param number number of custom action - used for error reporting
	 */
	public static void mimActionPerformed(
		@NotNull AnActionEvent e,
		@NotNull String code,
		int number
	) {
		try {
			final List<Token> tokens = new Lexer(code).lex();
			final List<Node> nodes = new Parser(tokens).parse();
			
			final ActionableContext context = new ActionableContext(e);
			final Interpreter interpreter = new Interpreter(context);
			for (Node node : nodes) interpreter.interpret(node);
		} catch (Exception ex) {
			Messages.showErrorDialog(ex.getMessage() + Arrays.toString(ex.getStackTrace()), "Error in CustomAction" + number + ":");
			ex.printStackTrace();
		}
	}
}

package ir.mmd.intellijDev.Actionable.action;

/**
 * List of action names needed by this plugin
 */
public final class Actions {
	private Actions() {
	}
	
	public static final String MACRO_PREFIX = "Actionable.Macro";
	
	/**
	 * List of action group names needed bt this plugin
	 */
	public static final class Groups {
		private Groups() {
		}
		
		public static final String MACROS = "ir.mmd.intellijDev.Actionable.text.macro.MacrosActionGroup";
	}
}

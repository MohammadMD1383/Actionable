package ir.mmd.intellijDev.Actionable.scripting.settings;

import javax.swing.*;

public class UI {
	private JPanel component;
	private JTextArea actionCode1;
	private JTextArea actionCode2;
	private JTextArea actionCode3;
	private JTextArea actionCode4;
	private JTextArea actionCode5;
	private JTextArea actionCode6;
	private JTextArea actionCode7;
	private JTextArea actionCode8;
	private JTextArea actionCode9;
	
	public JPanel getComponent() { return component; }
	
	public String getActionCode1() { return actionCode1.getText(); }
	public String getActionCode2() { return actionCode2.getText(); }
	public String getActionCode3() { return actionCode3.getText(); }
	public String getActionCode4() { return actionCode4.getText(); }
	public String getActionCode5() { return actionCode5.getText(); }
	public String getActionCode6() { return actionCode6.getText(); }
	public String getActionCode7() { return actionCode7.getText(); }
	public String getActionCode8() { return actionCode8.getText(); }
	public String getActionCode9() { return actionCode9.getText(); }
	
	public void setActionCode1(String code) { actionCode1.setText(code); }
	public void setActionCode2(String code) { actionCode2.setText(code); }
	public void setActionCode3(String code) { actionCode3.setText(code); }
	public void setActionCode4(String code) { actionCode4.setText(code); }
	public void setActionCode5(String code) { actionCode5.setText(code); }
	public void setActionCode6(String code) { actionCode6.setText(code); }
	public void setActionCode7(String code) { actionCode7.setText(code); }
	public void setActionCode8(String code) { actionCode8.setText(code); }
	public void setActionCode9(String code) { actionCode9.setText(code); }
}

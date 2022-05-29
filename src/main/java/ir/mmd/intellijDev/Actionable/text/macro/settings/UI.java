package ir.mmd.intellijDev.Actionable.text.macro.settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

public class UI {
	private final Object[] columnNames = new Object[]{"Name", "Macro"};
	
	private JPanel component;
	private JTable macrosTable;
	
	public JPanel getComponent() {
		return component;
	}
	
	public Map<String, String> getMacros() {
		Map<String, String> macros = new HashMap<>();
		
		for (int i = 0; i < macrosTable.getRowCount(); i++) {
			String name = (String) macrosTable.getValueAt(i, 0);
			String macro = (String) macrosTable.getValueAt(i, 1);
			if (name.trim().isEmpty() || macro.isEmpty()) continue;
			
			macros.put(name, macro);
		}
		
		return macros;
	}
	
	public void setMacros(Map<String, String> macros) {
		Object[][] mappedMacros = new Object[macros.size()][2];
		int counter = 0;
		
		for (Map.Entry<String, String> e : macros.entrySet()) {
			mappedMacros[counter][0] = e.getKey();
			mappedMacros[counter][1] = e.getValue();
			counter++;
		}
		
		macrosTable.setModel(new DefaultTableModel(mappedMacros, columnNames));
	}
}

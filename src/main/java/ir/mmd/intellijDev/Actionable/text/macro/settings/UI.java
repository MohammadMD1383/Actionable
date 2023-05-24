package ir.mmd.intellijDev.Actionable.text.macro.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBList;
import ir.mmd.intellijDev.Actionable.text.macro.MacroUtilKt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UI implements Disposable {
	private final Path macroStorePath = MacroUtilKt.getMacroStorePath();
	private JScrollPane component;
	private JLabel pathLabel;
	private JBList<String> macroList;
	private JButton createNewMacroButton;
	private JButton removeSelectedMacroButton;
	private JSplitPane splitPane;
	private final Project project;
	private FileEditor editor;
	
	public JScrollPane getComponent() {
		return component;
	}
	
	public UI() {
		final var projectManager = ProjectManager.getInstance();
		final var openProjects = projectManager.getOpenProjects();
		project = openProjects.length > 0 ? openProjects[0] : projectManager.getDefaultProject();
		
		pathLabel.setText("Macro store path: " + macroStorePath);
		
		new ListSpeedSearch<>(macroList);
		macroList.addListSelectionListener(this::listSelectionChanged);
		var macroNames = MacroUtilKt.getMacroNames();
		var model = new DefaultListModel<String>();
		model.addAll(macroNames);
		macroList.setModel(model);
		
		if (macroNames != null && !macroNames.isEmpty()) {
			macroList.setSelectedIndex(0);
		}
		
		createNewMacroButton.addActionListener(e -> {
			var newMacroName = JOptionPane.showInputDialog(component, "Please enter name for the new macro", "New Macro");
			
			try {
				Files.createFile(Paths.get(macroStorePath.toString(), newMacroName + ".mt"));
				model.add(0, newMacroName + ".mt");
				macroList.setSelectedIndex(0);
			} catch (FileAlreadyExistsException ignored) {
				JOptionPane.showMessageDialog(component, "Macro with the same name already exists", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
		
		removeSelectedMacroButton.addActionListener(e -> {
			if (macroList.isSelectionEmpty()) {
				return;
			}
			
			var macroName = macroList.getSelectedValue();
			
			try {
				closeEditor();
				Files.delete(Paths.get(macroStorePath.toString(), macroName));
				model.remove(macroList.getSelectedIndex());
				
				if (!model.isEmpty()) {
					macroList.setSelectedIndex(0);
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	
	void listSelectionChanged(ListSelectionEvent e) {
		if (macroList.isSelectionEmpty()) {
			return;
		}
		
		var selectedMacro = Paths.get(macroStorePath.toString(), macroList.getSelectedValue());
		openEditor(selectedMacro);
	}
	
	private void openEditor(Path file) {
		closeEditor();
		
		editor = PsiAwareTextEditorProvider.getInstance().createEditor(
			project, LocalFileSystem.getInstance().findFileByNioFile(file)
		);
		
		splitPane.setRightComponent(editor.getComponent());
	}
	
	private void closeEditor() {
		if (editor != null) {
			splitPane.remove(editor.getComponent());
			Disposer.dispose(editor);
			editor = null;
		}
	}
	
	public void saveChanges() {
		final var documentManager = FileDocumentManager.getInstance();
		documentManager.saveDocument(
			documentManager.getDocument(editor.getFile())
		);
	}
	
	@Override
	public void dispose() {
		closeEditor();
	}
}

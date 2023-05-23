package ir.mmd.intellijDev.Actionable.text.macro.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBList;
import ir.mmd.intellijDev.Actionable.text.macro.MacroUtilKt;
import ir.mmd.intellijDev.Actionable.text.macro.lang.MacroTemplateFileType;
import ir.mmd.intellijDev.Actionable.vfs.MemoryMappedVirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class UI implements Disposable {
	private final Path macroStorePath = MacroUtilKt.getMacroStorePath();
	private JScrollPane component;
	private JLabel pathLabel;
	private JBList<String> macroList;
	private Editor macroEditor;
	private JComponent macroEditorComponent;
	private JButton createNewMacroButton;
	private JButton removeSelectedMacroButton;
	
	public JScrollPane getComponent() {
		return component;
	}
	
	public UI() {
		pathLabel.setText("Macro store path: " + macroStorePath);
		
		new ListSpeedSearch<>(macroList);
		macroList.addListSelectionListener(this::listSelectionChanged);
		var macroNames = MacroUtilKt.getMacroNames();
		var model = new DefaultListModel<String>();
		model.addAll(macroNames);
		macroList.setModel(model);
		
		if (macroNames == null || macroNames.isEmpty()) {
			macroEditorComponent.setVisible(false);
		} else {
			macroList.setSelectedIndex(0);
		}
		
		createNewMacroButton.addActionListener(e -> {
			var newMacroName = JOptionPane.showInputDialog(component, "Please enter name for the new macro", "New Macro");
			
			try {
				Files.createFile(Paths.get(macroStorePath.toString(), newMacroName));
				model.add(0, newMacroName);
				
				if (model.size() == 1) {
					macroEditorComponent.setVisible(true);
				}
			} catch (FileAlreadyExistsException ignored) {
				JOptionPane.showMessageDialog(component, "Macro with the same name already exists", "Error", JOptionPane.ERROR_MESSAGE);
				
				if (model.isEmpty()) {
					macroEditorComponent.setVisible(false);
				}
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
				Files.delete(Paths.get(macroStorePath.toString(), macroName));
				model.remove(macroList.getSelectedIndex());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	
	private void createUIComponents() {
		var factory = EditorFactory.getInstance();
		var document = factory.createDocument("");
		macroEditor = factory.createEditor(document, null, new MemoryMappedVirtualFile("Macro", "", MacroTemplateFileType.INSTANCE), false, EditorKind.MAIN_EDITOR); // fixme
		macroEditorComponent = PsiAwareTextEditorProvider.getInstance().createEditor(
			ProjectManager.getInstance().getOpenProjects()[0],
			new LightVirtualFile("File", MacroTemplateFileType.INSTANCE,"")
		).getComponent(); // todo
		
		document.addDocumentListener(new DocumentListener() {
			@Override
			public void documentChanged(@NotNull DocumentEvent event) {
				var text = event.getDocument().getText();
				try {
					Files.write(Paths.get(macroStorePath.toString(), macroList.getSelectedValue()), text.getBytes());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	void listSelectionChanged(ListSelectionEvent e) {
		if (macroList.isSelectionEmpty()) {
			return;
		}
		
		var selectedMacro = Paths.get(macroStorePath.toString(), macroList.getSelectedValue());
		
		try (var lines = Files.lines(selectedMacro)) {
			ApplicationManager.getApplication().runWriteAction(
				() -> macroEditor.getDocument().setText(lines.collect(Collectors.joining("\n")))
			);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public void dispose() {
		EditorFactory.getInstance().releaseEditor(macroEditor);
	}
}

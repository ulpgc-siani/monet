package org.monet.editor.ui.preference;


import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.monet.editor.Constants;
import org.monet.editor.MonetPreferences;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	DirectoryFieldEditor homePathEditor;

	public PreferencePage() {
		setPreferenceStore(MonetPreferences.getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		homePathEditor = new DirectoryFieldEditor(Constants.HOME, Constants.HOME_LABEL, parent);
		addField(homePathEditor);
	}

	@Override
	public boolean performOk() {
		if (super.performOk()) {
			String homePath = homePathEditor.getStringValue();
			MonetPreferences.setProperty(Constants.HOME, homePath);
			return true;
		}
		return false;
	}

}
package org.monet.editor.ui.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.monet.editor.ui.framework.SelectFieldsDialog;

public class GenerateSchemaFormDialog extends SelectFieldsDialog {

	public GenerateSchemaFormDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	public void create() {
		super.create();
		setTitle("Generate schema from fields");
		setMessage("Insert a form's schema from selected fields", IMessageProvider.INFORMATION);
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		super.createDialogArea(container);
		return container;
	}

}

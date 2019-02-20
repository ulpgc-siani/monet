package org.monet.editor.ui.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.monet.editor.ui.framework.SelectFieldsDialog;

public class GenerateViewFormDialog extends SelectFieldsDialog {
	private boolean isWidget;
	private String name;

	private Text textName;
	private Label labelName;

	public GenerateViewFormDialog(Shell parentShell) {
		super(parentShell);
		this.name = "";
		this.isWidget = false;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Generate view from fields");
		setMessage("Insert a form's view from selected fields", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button button = new Button(container, SWT.CHECK);
		button.setText("Is a widget");
		button.setSelection(isWidget);
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button button = (Button) event.getSource();
				isWidget = button.getSelection();
				refresh();
			}
		});

		labelName = new Label(container, SWT.NONE);
		labelName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName = new Text(container, SWT.BORDER);
		textName.setText(this.name);
		textName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				name = textName.getText();
			}
		});
		
		super.createDialogArea(container);
		return container;
	}

	@Override
	protected void refresh() {
		super.refresh();
		labelName.setText(isWidget ? "Name" : "Label");
	}

	public boolean isWidget() {
		return isWidget;
	}

	public void setIsWidget(boolean isWidget) {
		this.isWidget = isWidget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

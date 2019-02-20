package org.monet.editor.ui.wizards.newlayout;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SetupPage extends WizardPage {
	private Composite container;
	private Text textName;
	
	public SetupPage(IProject project) {
		super("SetupProject", "Monet Layout File", null);
		setDescription("Create a Monet Layout File in the layouts directory");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());

		ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				validatePage();
			}
		};

		Label label;

		label = new Label(container, SWT.NONE);
		label.setText("Name");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName = new Text(container, SWT.BORDER);
		textName.setText("");
		textName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textName.addModifyListener(modifyListener);

		container.pack(true);
		setControl(container);
		setPageComplete(false);
	}

	protected boolean validatePage() {
		setPageComplete(false);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus status = workspace.validateName(getName(), IResource.PROJECT);

		if (!status.isOK()) {
			setErrorMessage(status.getMessage());
			return false;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getName());
		if (project.exists()) {
			setErrorMessage("A layout file with with the same name already exists in this project");
			return false;
		}

		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	public String getName() {
		return textName.getText().trim();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}

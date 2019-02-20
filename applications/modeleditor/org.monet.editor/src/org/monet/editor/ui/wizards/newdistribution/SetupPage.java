package org.monet.editor.ui.wizards.newdistribution;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

@SuppressWarnings("restriction")
public class SetupPage extends NewTypeWizardPage {

	private Composite container;
	private Text textName;

	public SetupPage() {
	  super(true, "SetupDistribution");
    setTitle("Monet Distribution");
    setDescription("Create a New Distribution in the Monet Project");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
	    
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
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

		IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
		IStatus status = workspace.validateName(getName(), IResource.PROJECT);

		if (!status.isOK()) {
			setErrorMessage(status.getMessage());
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

  public IProject getProject() {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    ISelectionService service = window.getSelectionService();
    ISelection selection = service.getSelection();
    if (selection instanceof IStructuredSelection) {
      IJavaElement element = getInitialJavaElement((IStructuredSelection) selection);
      return element.getJavaProject().getProject();
    }
    return null;
  }

}

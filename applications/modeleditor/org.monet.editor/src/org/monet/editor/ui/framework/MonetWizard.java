package org.monet.editor.ui.framework;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.monet.editor.core.commands.IJobCommand;

public abstract class MonetWizard extends Wizard {

	protected IWorkbenchWindow window;
	protected IWorkbench workbench;
	protected IStructuredSelection selection;
	private IWizardPage lastPage;

	public MonetWizard() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
		this.workbench = window.getWorkbench();
		this.selection = (IStructuredSelection) window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		this.window = workbench.getActiveWorkbenchWindow();
	}

	@Override
	public void addPage(IWizardPage page) {
		super.addPage(page);
		lastPage = page;
	}
	
	public IWizardPage getNextPage() {
		return null;
	}

	@Override
	public boolean canFinish() {
		if (lastPage == null)
			return false;
		if (getContainer().getCurrentPage() != this.lastPage)
			return false;
		return this.lastPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		MonetJob.execute(this.getWindowTitle(), createCommand());
		return true;
	}

	protected abstract IJobCommand createCommand();

}

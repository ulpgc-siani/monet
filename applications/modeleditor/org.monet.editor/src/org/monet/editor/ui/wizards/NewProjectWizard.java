package org.monet.editor.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.monet.editor.core.commands.CreateProjectCommand;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.ui.framework.MonetWizard;
import org.monet.editor.ui.wizards.newproject.SetupPage;

public class NewProjectWizard extends MonetWizard implements INewWizard {
	private SetupPage setup;

	public NewProjectWizard() {
		setWindowTitle("New Project");
	}

	@Override
	public void addPages() {
		this.setup = new SetupPage();	
		this.addPage(setup);
	}
	
	@Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
    }

	@Override
	protected IJobCommand createCommand() {
	  CreateProjectCommand.Parameters parameters = new CreateProjectCommand.Parameters();
		parameters.name = setup.getName();
		parameters.basePackage = setup.getBasePackage();
		parameters.template = setup.getTemplate();
		parameters.parent = setup.getParent();
		return new CreateProjectCommand(parameters);
	}	

}

package org.monet.editor.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.monet.editor.MonetLog;
import org.monet.editor.core.commands.CreateDefinitionCommand;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.ui.ProjectManager;
import org.monet.editor.ui.framework.MonetWizard;
import org.monet.editor.ui.wizards.newdefinition.DocumentPage;
import org.monet.editor.ui.wizards.newdefinition.SelectionPage;
import org.monet.editor.ui.wizards.newdefinition.SetupPage;

public class NewDefinitionWizard extends MonetWizard implements INewWizard {
  private IProject project;
  
	private SetupPage setup;
	private SelectionPage select;
	private DocumentPage defineDocument;

	public NewDefinitionWizard() {
		setWindowTitle("New Definition");		
	}

	@Override
	public void addPages() {
		this.setup = new SetupPage(this.project);
		this.select = new SelectionPage();
		this.defineDocument = new DocumentPage();
		this.defineDocument.setNextPage(this.setup);
		this.addPage(this.select);
		this.addPage(this.defineDocument);
		this.addPage(this.setup);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		
		this.project = ProjectManager.instance().getActiveProject(this.selection);
		if(project == null) {
		  EclipseHelper.showErrorMessage("No project selected");
		  throw new RuntimeException("No project selected.");
		}
	}

	@Override
	public IJobCommand createCommand() {
		CreateDefinitionCommand.Parameters parameters = new CreateDefinitionCommand.Parameters();
		parameters.project = this.project;
		parameters.name = setup.getName();
		parameters.packageName = setup.getPackageName();
		parameters.template = select.getTemplate();
		
		MonetLog.print("Name: " + parameters.name);
		MonetLog.print("PackageName: " + parameters.packageName);
		MonetLog.print("Template: " + parameters.template);
		
		return new CreateDefinitionCommand(parameters);
	}

	@Override
	public IWizardPage getNextPage() {
		String className = this.select.getClassName();
		if (className.equals("document"))
			return this.defineDocument;
		else
			return this.setup;
	}


}

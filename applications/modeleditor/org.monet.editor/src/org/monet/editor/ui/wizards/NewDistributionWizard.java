package org.monet.editor.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.monet.editor.core.commands.CreateDistributionCommand;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.ui.framework.MonetWizard;
import org.monet.editor.ui.wizards.newdistribution.SetupPage;

public class NewDistributionWizard extends MonetWizard implements INewWizard {
  private SetupPage setup;
  private IProject  project;

  public NewDistributionWizard() {
    setWindowTitle("New Distribution");
  }

  public IProject getProject() {
    return project;
  }

  public void setProject(IProject project) {
    this.project = project;
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
    CreateDistributionCommand.Parameters parameters = new CreateDistributionCommand.Parameters();
    parameters.project = this.project != null ? this.project : setup.getProject();
    parameters.name = setup.getName();
    return new CreateDistributionCommand(parameters);

  }

}

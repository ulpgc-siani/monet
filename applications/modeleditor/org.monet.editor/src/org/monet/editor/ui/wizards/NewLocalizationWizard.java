package org.monet.editor.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.monet.editor.core.commands.CreateLocalizationCommand;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.ui.ProjectManager;
import org.monet.editor.ui.framework.MonetWizard;
import org.monet.editor.ui.wizards.newlocalization.SetupPage;

public class NewLocalizationWizard extends MonetWizard implements INewWizard {
  private IProject project;
  
  private SetupPage setup;

  public NewLocalizationWizard() {
    setWindowTitle("New Layout File");
  }

  @Override
  public void addPages() {
    this.setup = new SetupPage(this.project);
    this.addPage(setup);
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    super.init(workbench, selection);

    this.project = ProjectManager.instance().getActiveProject(this.selection);
    if (project == null) {
      EclipseHelper.showErrorMessage("No project selected");
      throw new RuntimeException("No project selected.");
    }
  }

  @Override
  protected IJobCommand createCommand() {
    CreateLocalizationCommand.Parameters parameters = new CreateLocalizationCommand.Parameters();
    parameters.project = this.project;
    parameters.name = setup.getName();
    return new CreateLocalizationCommand(parameters);
  }

}

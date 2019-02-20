package org.monet.editor.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.monet.editor.core.commands.CreateLayoutCommand;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.ui.ProjectManager;
import org.monet.editor.ui.framework.MonetWizard;
import org.monet.editor.ui.wizards.newlayout.SetupPage;

public class NewLayoutWizard extends MonetWizard implements INewWizard {
  private IProject project;
  
  private SetupPage setup;

  public NewLayoutWizard() {
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
    CreateLayoutCommand.Parameters parameters = new CreateLayoutCommand.Parameters();
    parameters.name = setup.getName();
    parameters.project = this.project;
    return new CreateLayoutCommand(parameters);
  }

}

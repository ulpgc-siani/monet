package org.monet.editor.core.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.monet.editor.MonetLog;
import org.monet.editor.core.wrappers.DistributionWrapper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.net.DeployServiceClient;
import org.monet.editor.net.DeployServiceClient.InvalidSpaceException;

public class RestartSpaceCommand implements IJobCommand {

  private IResource distribution;

  public RestartSpaceCommand(IResource project, IResource distribution) {
    super();
    this.distribution = distribution;
  }

  @Override
  public void execute(IProgressMonitor monitor) {
    monitor.beginTask("Working...", 0);

    String deployUri = null;

    try {
      DistributionWrapper distributionModel = DistributionWrapper.fromFile((IFile) this.distribution);
      deployUri = distributionModel.getDeployUri();

      if (deployUri == null) {
        MonetLog.print("Not found a deploy-uri at distribution definition. Publish finish here.");
      } else {
        try {
          if (!DeployServiceClient.restartSpace(deployUri, distributionModel.getName())) {
            throw new RuntimeException();
          }
        } catch (InvalidSpaceException e) {
          EclipseHelper.showErrorMessage(String.format("Space %s doesn't exists, create it first..", distributionModel.getName()));
        }
      }
    } catch (Exception e) {
      MonetLog.print(e);
      EclipseHelper.showErrorMessage("Error executing the operation. See Error Log for more details.");
    } finally {
    }
    monitor.done();
  }

  @Override
  public boolean canExecute() {
    MessageDialog dialog = new MessageDialog(PlatformUI
        .getWorkbench().getActiveWorkbenchWindow()
        .getShell(), 
        "Restart space", 
        null, "Are you sure you want to restart space?", 
        MessageDialog.QUESTION, 
        new String[] { "Yes", "No" }, 0);
    int result = dialog.open();
    return result == 0;
  }

}

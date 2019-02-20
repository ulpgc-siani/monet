package org.monet.editor.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ResourceManager;
import org.monet.editor.library.EclipseHelper;

public class LaunchMSManagerCommand extends Command implements IJobCommand {

  public static class MSManagerInstance {
    public static MSManagerInstance instance;

    private MSManagerInstance() {
    }

    public static MSManagerInstance getInstance() {
      if (instance == null)
        instance = new MSManagerInstance();
      return instance;
    }

    public void launch(final IAction sender) {
      Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
          try {
            String execTemplate = null;
            String path = ResourceManager.getInstance().getMvmmanagerPath();
            MonetLog.print(path);
            String[] command = null;

            if (isWindows()) {
              execTemplate = Constants.MSMANAGER_WIN;
              command = new String[] { String.format(execTemplate, path), path };
            } else if (isMac()) {
              execTemplate = Constants.MSMANAGER_MAC;
              command = new String[] { "sh", String.format(execTemplate, path), path };
            } else {
              execTemplate = Constants.MSMANAGER_LINUX;
              command = new String[] { "sh", String.format(execTemplate, path), path };
            }

            Runtime.getRuntime().exec(command).waitFor();
            sender.setEnabled(true);
          } catch (Exception e) {
            MonetLog.print(e);
            EclipseHelper.showErrorMessage("Error executing the operation. See Error Log for more details.");
          } finally {
          }
        }
      });
      thread.start();
    }

  }

  private IAction       sender;

  public LaunchMSManagerCommand(IAction sender) {
    this.sender = sender;
  }

  @Override
  public void execute(IProgressMonitor monitor) {
    monitor.beginTask("Opening MSManager, please wait...", 0);

    if (sender.isEnabled()) {
      this.sender.setEnabled(false);
      MSManagerInstance.getInstance().launch(sender);
      monitor.done();
    }
  }

  @Override
  public boolean canExecute() {
    return true;
  }

}

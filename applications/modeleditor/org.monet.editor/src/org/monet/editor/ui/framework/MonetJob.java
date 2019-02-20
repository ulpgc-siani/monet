package org.monet.editor.ui.framework;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.monet.editor.MonetLog;
import org.monet.editor.core.commands.IJobCommand;
import org.monet.editor.library.EclipseHelper;

public class MonetJob {

  public static void execute(final String name, final IJobCommand command) {
    if (command.canExecute()) {
      Job job = new Job(name) {
        @Override
        protected IStatus run(IProgressMonitor monitor) {
          try {
            if (monitor.isCanceled())
              return Status.CANCEL_STATUS;

            command.execute(monitor);
          } catch (Exception e) {
            MonetLog.print(e);

            EclipseHelper.showErrorMessage("Can't execute command: " + e.getMessage());
          }
          return Status.OK_STATUS;
        }
      };
      job.setPriority(Job.SHORT);
      job.schedule();
    }
  }

}

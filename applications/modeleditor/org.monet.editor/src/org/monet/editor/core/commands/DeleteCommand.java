package org.monet.editor.core.commands;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.MonetLog;

public class DeleteCommand implements IJobCommand {
	
	private IResource setup;

	public DeleteCommand(IResource setup) {
		super();
		this.setup = setup;
	}

	@Override
	public void execute(IProgressMonitor monitor) {
		monitor.beginTask("Working...", 0);
		try {
      setup.getParent().delete(true, monitor);
    } catch (CoreException e) {
      MonetLog.print(e);
    }
		monitor.done();
	}

  @Override
  public boolean canExecute() {
    return true;
  }
	
}

package org.monet.editor.core.commands;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class RenameCommand implements IJobCommand {
	
	private IResource setup;
	private String newName;

	public RenameCommand(IResource setup, String newName) {
		super();
		this.setup = setup;
		this.newName = newName;
	}

	@Override
	public void execute(IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Working...", 0);
		setup.getParent().move(setup.getParent().getFullPath().removeLastSegments(1).append(this.newName), true, monitor);
		monitor.done();
	}

  @Override
  public boolean canExecute() {
    return true;
  }

}

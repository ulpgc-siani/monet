package org.monet.editor.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IJobCommand {
	
  public boolean canExecute();
	public void execute(IProgressMonitor monitor) throws Exception;
	
}

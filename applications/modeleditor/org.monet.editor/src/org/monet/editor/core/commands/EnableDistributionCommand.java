package org.monet.editor.core.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;

public class EnableDistributionCommand implements IJobCommand {
	
	private IResource setup;

	public EnableDistributionCommand(IResource setup) {
		super();
		this.setup = setup;
	}

	@Override
	public void execute(IProgressMonitor monitor) {
		monitor.beginTask("Working...", 0);
		try {
			IResource resourceParent = setup.getParent();
			if (resourceParent instanceof IFolder) {
				IFolder folderDistribution = (IFolder) resourceParent;
				IFile fileDisabled = folderDistribution.getFile(Constants.DISTRIBUTION_DISABLED_FILE);
				if ((fileDisabled != null) && (fileDisabled.exists()))
					fileDisabled.delete(true, monitor);
			}

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

package org.monet.editor.core.commands;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.Constants;
import org.monet.editor.MonetPreferences;
import org.monet.editor.library.EclipseHelper;

public class CreateLayoutCommand implements IJobCommand {

	public static class Parameters {
	  public IProject project;
		public String name;
	}

	private Parameters parameters;

	public CreateLayoutCommand(Parameters parameters) {
		this.parameters = parameters;
	}

	public void execute(IProgressMonitor monitor) throws Exception {
	  monitor.beginTask("Working...", 0);
	  
    if(this.parameters.project == null) {
      EclipseHelper.showErrorMessage("No project selected.");
      return;
    }
    
    IFolder layoutsFolder = this.parameters.project.getFolder(Constants.RES_LAYOUTS_FOLDER);
    if (!layoutsFolder.exists())
      layoutsFolder.create(true, true, monitor);
    
    File sourceFile = new File(MonetPreferences.getTemplateLayoutPath() + "/layout.xml");
    IFolder folder = this.parameters.project.getFolder(Constants.RES_LAYOUTS_FOLDER);
    IFile destination = folder.getFile(this.parameters.name + ".xml");
    
    EclipseHelper.createFile(destination, new FileInputStream(sourceFile), monitor);
    
	  monitor.done();
	}

	@Override
	public boolean canExecute() {
		return true;
	}

}

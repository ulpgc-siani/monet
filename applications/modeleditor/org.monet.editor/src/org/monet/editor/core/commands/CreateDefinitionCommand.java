package org.monet.editor.core.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.core.generators.PatternFileGenerator;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryFilesystem;


public class CreateDefinitionCommand implements IJobCommand {

  public static class Parameters {
    
    public IProject project;
    public String name;
    public String packageName;
    public String template;

  }
  
	private Parameters parameters;

	public CreateDefinitionCommand(Parameters parameters) {
		this.parameters = parameters;
	}

  @Override
  public void execute(IProgressMonitor monitor) throws Exception {
    monitor.beginTask("Working...", 0);

    if(this.parameters.project == null) {
      EclipseHelper.showErrorMessage("No project selected.");
      return;
    }
    
    IFolder packageFolder = this.parameters.project.getFolder(Constants.SOURCE_FOLDER + "/" + this.parameters.packageName.replaceAll("\\.", "/"));
    EclipseHelper.createFolder(packageFolder, monitor);
    
    List<File> files = new ArrayList<File>();
    LibraryFilesystem.listFiles(this.parameters.template, files, true);

    for (File file : files){
      try {
        
        if(file.getName().startsWith(".")) {
          continue;
        }
        
        PatternFileGenerator.Parameters parameters = new PatternFileGenerator.Parameters();
        parameters.basePackage = ProjectHelper.getProjectPackageBase(this.parameters.project);
        parameters.currentPackage = this.parameters.packageName;
        parameters.name = this.parameters.name;
        
        parameters.outputFile = packageFolder.getFile(file.getName().replaceAll("\\$name", this.parameters.name));
        parameters.patternFilename = file.getName();
        parameters.patternPath = this.parameters.template;
        
        PatternFileGenerator pfg = new PatternFileGenerator(monitor, parameters);
        pfg.create();
      } catch (Throwable ex) {
        MonetLog.print(ex);
      }
    }
    
    monitor.done();
  }

  @Override
  public boolean canExecute() {
    return true;
  }

}

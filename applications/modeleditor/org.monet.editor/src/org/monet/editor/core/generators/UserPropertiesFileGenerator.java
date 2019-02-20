package org.monet.editor.core.generators;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.EclipseHelper;

public class UserPropertiesFileGenerator extends FileGenerator {

  public static class Parameters {
    public String path;
  }
  
  private IProject project; 
  private IProgressMonitor monitor;
  private Parameters parameters;

  public UserPropertiesFileGenerator(IProject project, IProgressMonitor monitor, Parameters parameters) {
    this.project = project;
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    IFile userPropertiesFile = ProjectHelper.getUserPreferencesPath(this.project);
    
    EclipseHelper.createFile(userPropertiesFile, this.getOutput(), monitor);

    return userPropertiesFile;
  }
  
  @Override
  protected void init() {
    loadCanvas("user.properties", true);
    
    addMark("path", this.parameters.path);
  }

}

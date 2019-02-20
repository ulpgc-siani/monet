package org.monet.editor.core.generators;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.EclipseHelper;

public class ProjectPropertiesFileGenerator extends FileGenerator {

  public static class Parameters {
    public String packageName;
    public String release;
    public String parent;
  }
  
  private IProject project; 
  private IProgressMonitor monitor;
  private Parameters parameters;

  public ProjectPropertiesFileGenerator(IProject project, IProgressMonitor monitor, Parameters parameters) {
    this.project = project;
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    IFile propertiesFile = ProjectHelper.getPreferencesPath(this.project);
    
    EclipseHelper.createFile(propertiesFile, this.getOutput(), monitor);

    return propertiesFile;
  }
  
  @Override
  protected void init() {
    loadCanvas("properties", true);
    
    addMark(ProjectHelper.PROJECT_PACKAGE_BASE, this.parameters.packageName);
    addMark(ProjectHelper.PROJECT_RELEASE_NUMBER, this.parameters.release);
    addMark(ProjectHelper.PROJECT_PARENT_PROJECT, this.parameters.parent);
  }

}

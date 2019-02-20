package org.monet.editor.core.generators;

import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.EclipseHelper;

public class ProjectFileGenerator extends FileGenerator {

  public static class Parameters {
    public String name;
    public String author;
  }
  
  private IProject project; 
  private IProgressMonitor monitor;
  private Parameters parameters;

  public ProjectFileGenerator(IProject project, IProgressMonitor monitor, Parameters parameters) {
    this.project = project;
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    IFile projectFile = ProjectHelper.getProjectFile(this.project);
    
    EclipseHelper.createFile(projectFile, this.getOutput(), monitor);
    
    return projectFile;
  }
  
  @Override
  protected void init() {
    loadCanvas("project", true);
    
    addMark("uuid", UUID.randomUUID().toString());
    addMark("name", this.parameters.name);
    addMark("author", this.parameters.author);
  }
  
}

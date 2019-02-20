package org.monet.editor.core.generators;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.library.EclipseHelper;

public class DistributionFileGenerator extends FileGenerator {

  public static class Parameters {
    public String name;
    public IFile outputFile;
    public String project;
  }
  
  private IProgressMonitor monitor;
  private Parameters parameters;

  public DistributionFileGenerator(IProgressMonitor monitor, Parameters parameters) {
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    EclipseHelper.createFile(this.parameters.outputFile, this.getOutput(), monitor);
    
    return this.parameters.outputFile;
  }
  
  @Override
  protected void init() {
    loadCanvas("distribution", true);
    
    addMark("project", this.parameters.project);
    addMark("name", this.parameters.name);
  }
  
}

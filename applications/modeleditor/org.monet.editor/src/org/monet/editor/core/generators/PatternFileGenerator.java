package org.monet.editor.core.generators;

import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryCodeGenerator;

public class PatternFileGenerator extends FileGenerator {

  public static class Parameters {
    public String name;
    public String project;
    public String basePackage;
    public String currentPackage;
    public String patternPath;
    public String patternFilename;
    public IFile outputFile;
  }
  
  private IProgressMonitor monitor;
  private Parameters parameters;

  public PatternFileGenerator(IProgressMonitor monitor, Parameters parameters) {
    super("");
    
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    if(this.parameters.outputFile.exists())
      throw new RuntimeException("File already exists " + this.parameters.outputFile.getName());
    
    EclipseHelper.createFile(this.parameters.outputFile, this.getOutput(), monitor);

    return this.parameters.outputFile;
  }

  @Override
  protected void init() {
    loadCanvas(this.parameters.patternPath + "/" + this.parameters.patternFilename, true);
    
    addMark("name", this.parameters.name);
    addMark("code", new Object(){ 
      @Override
      public String toString() {
        return LibraryCodeGenerator.generateCode(parameters.name);
      }
    });
    addMark("uuid", new Object(){ 
      @Override
      public String toString() {
        return UUID.randomUUID().toString();
      }
    });
    addMark("project", this.parameters.project);
    addMark("base-package", this.parameters.basePackage);
    addMark("current-package", this.parameters.currentPackage != null ? this.parameters.currentPackage : "");
  }
  
}

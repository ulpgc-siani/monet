package org.monet.editor.core.generators;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.monet.editor.library.EclipseHelper;

@SuppressWarnings("restriction")
public class ResourcesClassFileGenerator extends FileGenerator {

  public static class Parameters {
    public IPath filePath;
    public String packageName;
    public HashMap<String, String> helpResources;
    public HashMap<String, String> imageResources;
    public HashMap<String, String> templateResources;
    public HashMap<String, String> layoutResources;
  }
  
  private IProgressMonitor monitor;
  private Parameters parameters;

  public ResourcesClassFileGenerator(IProgressMonitor monitor, Parameters parameters) {
    this.monitor = monitor;
    this.parameters = parameters;
  }
  
  public IFile create() throws Exception {
    IFile resourcesFile = IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(this.parameters.filePath);
    
    EclipseHelper.createFile(resourcesFile, this.getOutput(), monitor);

    return resourcesFile;
  }
  
  @Override
  protected void init() {
    loadCanvas("resources", true);
    
    addMark("package", this.parameters.packageName);
    
    addMark("helpResources", renderMap("entry", this.parameters.helpResources));
    addMark("imageResources", renderMap("entry", this.parameters.imageResources));
    addMark("templateResources", renderMap("entry", this.parameters.templateResources));
    addMark("layoutResources", renderMap("entry", this.parameters.layoutResources));
  }
  
  protected String renderMap(String entryBlock, HashMap<String, String> map) {
    StringBuilder builder = new StringBuilder();
    
    HashMap<String, Object> blockMap = new HashMap<String, Object>();
    
    for(java.util.Map.Entry<String, String> entry : map.entrySet()) {
      blockMap.put("key", entry.getKey());
      blockMap.put("value", entry.getValue());
      
      builder.append(block(entryBlock, blockMap));
    }
    
    return builder.toString();
  }
  
}

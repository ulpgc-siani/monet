package org.monet.editor.core.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.MonetRuntimeProblemMarker;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.core.util.ResourceManager;
import org.monet.editor.library.EclipseHelper;

public class PreviewCommand implements IJobCommand {

  private IResource           manifest;
  private IResource           distribution;

  public PreviewCommand(IResource manifest, IResource distribution) {
    this.manifest = manifest;
    this.distribution = distribution;
  }

  @Override
  public void execute(IProgressMonitor monitor) throws Exception {
    final IProject project = manifest.getProject();

    MonetRuntimeProblemMarker.clean(project);
    if (ProjectHelper.hasError(project, true)) {
      EclipseHelper.showErrorMessage("Can't generate preview of projects with errors.");
      return;
    }

    monitor.beginTask("Working...", 0);

    String previewerPath = ResourceManager.getInstance().getInternalResource(Constants.PREVIEWER_EXECUTABLE_PATH);

    IFolder previewModelFolder = ProjectHelper.getTempFolder(project);
    String modelPath = previewModelFolder.getLocation().toString();
    if(previewModelFolder.exists()) {
      previewModelFolder.delete(true, monitor);
    }
    
    GeneratePackageCommand generatePkgCmd = new GeneratePackageCommand(this.manifest, this.distribution, previewModelFolder, "preview");
    generatePkgCmd.execute(monitor);
    
    final String previewPath = ProjectHelper.getPreviewFolder(project).getLocation().toString();
    String language = System.getProperty("user.language");

    int exitStatus = -1;
    AsyncStreamReader   outputStreamReader = null;
    try {
      ProcessBuilder builder = new ProcessBuilder(new String[]{"java", 
                                                               "-classpath", 
                                                               previewerPath, 
                                                               "org.monet.editor.preview.Main", 
                                                               String.format("\"%s\"", modelPath),  
                                                               String.format("\"%s\"", previewPath), 
                                                               language});
      builder.redirectErrorStream(true);
      Process process = builder.start();
      outputStreamReader = new AsyncStreamReader(process.getInputStream());
      outputStreamReader.start();
      exitStatus = process.waitFor();
      
      if(previewModelFolder.exists())
        previewModelFolder.delete(true, monitor);
    } catch (Exception e) {
      MonetLog.print(e);
      return;
    } finally {
      if(outputStreamReader != null)
        outputStreamReader.stopReading();
    }

    if (exitStatus == 0) {
      String previewerResult = outputStreamReader.getBuffer();
      
      JSONArray problemsArray = JSONArray.fromObject(previewerResult);
      Collection<?> problemsCollection = JSONArray.toCollection(problemsArray);
      if(problemsCollection.size() > 0) {
        for(Object item : problemsCollection) {
          JSONObject object = JSONObject.fromObject(item);
          MonetRuntimeProblemMarker.create(project, object.getString("message"));
        }
      
        EclipseHelper.showErrorMessage(String.format("%d errors found executing previewer.", problemsCollection.size()));
      } else {
        Display.getDefault().asyncExec(new Runnable() {
          
          @Override
          public void run() {
            IWebBrowser browser;
            try {
              browser = PlatformUI.getWorkbench()
                                                    .getBrowserSupport()
                                                    .createBrowser(IWorkbenchBrowserSupport.AS_EDITOR |
                                                                   IWorkbenchBrowserSupport.NAVIGATION_BAR, 
                                                                   null,
                                                                   "Preview of " + project.getName(),
                                                                   "Preview of " + project.getName());
              browser.openURL(new URL("file:///" + previewPath + "/index.html"));
            } catch (Exception e) {
              MonetLog.print(e);
            }
          }
        });
      }
    } else {
      MonetLog.print(new RuntimeException(outputStreamReader.getBuffer()));
      EclipseHelper.showErrorMessage("Error executing previewer. View ErrorLog for details.");
    }

    monitor.done();
  }

  class AsyncStreamReader extends Thread {
    private StringBuffer buffer      = new StringBuffer();
    private InputStream  inputStream = null;
    private boolean      stopFlag    = false;

    public AsyncStreamReader(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    public String getBuffer() {
      return buffer.toString();
    }

    public void run() {
      try {
        readCommandOutput();
      } catch (Exception ex) {
        MonetLog.print(ex);
      }
    }

    private void readCommandOutput() throws IOException {
      BufferedReader bufOut = new BufferedReader(new InputStreamReader(inputStream));
      int c = '\0';
      while ( (stopFlag == false) && ((c = bufOut.read()) != -1) ) 
        buffer.append((char)c);
      bufOut.close();
    }

    public void stopReading() {
      stopFlag = true;
    }

  }
  
  @Override
  public boolean canExecute() {
    return true;
  }

}

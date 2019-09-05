package org.monet.editor.core.commands;

import java.io.File;
import java.net.ConnectException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.core.wrappers.DistributionWrapper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.net.CountingMultipartEntity.ProgressListener;
import org.monet.editor.net.SpaceSetupServiceClient;
import org.monet.editor.ui.DeployConsoleManager;

public class RunCommand implements IJobCommand {
	
	private IResource manifest;
	private IResource distribution;
	private DeployConsoleManager console;

	public RunCommand(IResource manifest, IResource distribution) {
		super();
		this.manifest = manifest;
		this.distribution = distribution;
	}

	@Override
	public void execute(IProgressMonitor monitor) {
		monitor.beginTask("Working...", 0);
		
		final IProject project = manifest.getProject();
		String deployUri = null;
		String deployPath = null;
		
		if (ProjectHelper.hasError(project, true)) {
      EclipseHelper.showErrorMessage("Can't debug project with errors.");
      return;
    }
		
		try {
		  String projectName = project.getName();
		  final String distributionName = this.distribution.getParent().getName();
		  String version = ProjectHelper.getProjectVersion(project);
		  String release = "current";
		  String packageName = String.format("%s %s - %s (%s)", projectName, version, distributionName, release);
		  
		  console = DeployConsoleManager.buildNew(packageName);
		  console.println("Starting deploying of distribution %s from model %s", distributionName, projectName);
		  
		  console.println("Building package version %s", version, release);
		  
		  DistributionWrapper distributionModel = DistributionWrapper.fromFile((IFile)this.distribution);
		  IFile packageFile = ProjectHelper.getOutputFolder(manifest.getProject()).getFile(packageName + ".zip");
		  GeneratePackageCommand generatePkgCmd = new GeneratePackageCommand(this.manifest, this.distribution, packageFile, release);
		  generatePkgCmd.execute(monitor);
		        
      console.println("Package built at '%s'", packageFile.getFullPath().toString());
      
      deployUri = distributionModel.getDeployUri();
      deployPath = distributionModel.getDeployPath();
      
      if(deployUri == null) {
        console.println("Not found a deploy-uri at setup definition. Debug finish here.");
      } else {
        String keyStoreFilename = ProjectHelper.getProjectKeyStorePath(project);
        File keyStoreFile = new File(keyStoreFilename);
        if(!keyStoreFile.exists()) {
          console.error("KeyStore file not found. '%s'", keyStoreFilename);
        } else {
          console.println("Uploading distribution package to '%s'", deployUri);
          console.println("|--------- Progress ---------|");
          SpaceSetupServiceClient.publish(deployUri,
        		  						  deployPath,
                                          packageFile, 
                                          keyStoreFilename, 
                                          ProjectHelper.getProjectKeyStorePassword(project),
                                          console.getOutputStream(),
                                          new ProgressListener() {
                                            
                                            int last = 0;
            
                                            @Override
                                            public void transferred(long num, long total) {
//                                              int current = (int)(num*30/total);
//                                              for(int i=last;i<current;i++)
//                                                console.print("#");
                                            }
                                          });
          console.println("==============================");
          console.println("Deploy finished.");
        }
        
        final URL deployURL = new URL(deployUri);
        Display.getDefault().asyncExec(new Runnable() {
          
          @Override
          public void run() {
            IWebBrowser browser;
            try {
              String browserTitle = project.getName() + " " + distributionName;
              browser = PlatformUI.getWorkbench()
                                                    .getBrowserSupport()
                                                    .createBrowser(IWorkbenchBrowserSupport.AS_EDITOR |
                                                                   IWorkbenchBrowserSupport.NAVIGATION_BAR, 
                                                                   distributionName,
                                                                   browserTitle,
                                                                   browserTitle);
              browser.openURL(deployURL);
            } catch (Exception e) {
              MonetLog.print(e);
            }
          }
        });
      }
		} catch (ConnectException e) {
		  String errorMessage = e.getMessage() + " (" + deployUri + ")";
		  
		  MonetLog.print(errorMessage);
		  console.error(errorMessage);
    } catch (Exception e) {
      MonetLog.print(e);
      console.error(e.getMessage());
    } finally {
      if(console != null)
        console.close();
    }
		monitor.done();
	}

  @Override
  public boolean canExecute() {
    return true;
  }
	
}

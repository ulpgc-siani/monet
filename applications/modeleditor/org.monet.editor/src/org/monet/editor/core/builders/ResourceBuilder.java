package org.monet.editor.core.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.MonetResourceProblemMarker;
import org.monet.editor.core.generators.AssetsClassFileGenerator;
import org.monet.editor.core.generators.ResourcesClassFileGenerator;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.LibraryFile;
import org.monet.editor.library.LibraryString;

public class ResourceBuilder extends IncrementalProjectBuilder {

  private static final String ASSETS_JAVA_FILE = "Assets.java";
	private static final String RESOURCES_JAVA_FILE = "Resources.java";

	@SuppressWarnings("rawtypes")
  @Override
  protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
    IProject project = this.getProject();

    if (!this.didResourcesChange(project)) {
    	this.createResourceFoldersIfNotExist(monitor, project);
    	return null;
    }

    MonetResourceProblemMarker.clean(project);
    
    this.validateResources(monitor, project);
    this.generateResourcesClass(monitor, project);
    this.copyResourcesToOutputFolder(project);

    return null;
  }

  private IFolder getFolder(IContainer parent, String name, IProgressMonitor monitor) throws CoreException {
    IFolder newFolder = parent instanceof IFolder ? ((IFolder) parent).getFolder(name) : ((IProject) parent).getFolder(name);
    if (!newFolder.exists())
      newFolder.create(true, true, monitor);
    return newFolder;
  }

  private void createResourceFoldersIfNotExist(IProgressMonitor monitor, IProject project) throws CoreException {
 
	IFolder resFolder = this.getFolder(project, Constants.RES_FOLDER, monitor);
	
	for (Constants.RESOURCE resourceType : Constants.RESOURCE.values())
		this.getFolder(resFolder, resourceType.toString(), monitor);
  }

  private boolean didResourcesChange(IProject project) {
    IResourceDelta delta = getDelta(project);
    if (delta == null)
      return true;

    IResourceDelta[] folders = delta.getAffectedChildren();

    for (IResourceDelta resourceDelta : folders) {
      IResource resource = resourceDelta.getResource();
      if (resource.getName().equals(Constants.RES_FOLDER) ||
          resource.getName().equals(Constants.LIBS_FOLDER) || 
          resource.getName().equals(Constants.DIST_FOLDER))
        return true;
    }
    return false;
  }
  
  private boolean findImageFilename(IProgressMonitor monitor, IProject project, String name) throws CoreException {
    IFolder resFolder = this.getFolder(project, Constants.RES_FOLDER, monitor);
    IFolder modelImagesFolder = this.getFolder(resFolder, Constants.RESOURCE.images.toString(), monitor);
    
    return modelImagesFolder.getFile(name).exists();
  }
  
  private void validateModelResources(IProgressMonitor monitor, IProject project) throws CoreException {
  	
    for(String imageFilename : Constants.MANDATORY_MODEL_IMAGES) {
    	if (!this.findImageFilename(monitor, project, imageFilename)) {
        IFolder resFolder = this.getFolder(project, Constants.RES_FOLDER, monitor);
        IFolder modelImagesFolder = this.getFolder(resFolder, Constants.RESOURCE.images.toString(), monitor);
        MonetResourceProblemMarker.create(modelImagesFolder, "Missing required image: " + imageFilename);
    	}
    }
  }
  
  private void validateDistResources(IProgressMonitor monitor, IProject project) throws CoreException {
    for(IResource distribution : ProjectHelper.getDistFolder(project).members()) {
      if(distribution instanceof IFolder) {
        IFolder distributionFolder = (IFolder)distribution;
        IFolder distResFolder = this.getFolder(distributionFolder, Constants.RES_FOLDER, null);
        IFolder spaceImagesFolder = this.getFolder(distResFolder, Constants.RESOURCE.images.toString(), null);
        for(String imageFilename : Constants.MANDATORY_SPACE_IMAGES) {
          if(!spaceImagesFolder.getFile(imageFilename).exists()) {
            MonetResourceProblemMarker.create(spaceImagesFolder, "Missing required image: " + imageFilename);
          }
        }
      }
    }
  }
  
  private void validateResources(IProgressMonitor monitor, IProject project) throws CoreException {
  	this.validateModelResources(monitor, project);
  	this.validateDistResources(monitor, project);
  }
  
  private void copyResourcesToOutputFolder(IProject project) throws CoreException {
  	try {
      IFolder resOutputFolder = ProjectHelper.getBinFolder(project).getFolder(Constants.RES_FOLDER);
      if(resOutputFolder.exists())
        resOutputFolder.delete(true, true, null);
      ProjectHelper.getResourcesFolder(project).copy(resOutputFolder.getFullPath(), true, null);
      
      IFolder libsOutputFolder = ProjectHelper.getBinFolder(project).getFolder(Constants.LIBS_FOLDER);
      if(libsOutputFolder.exists())
        libsOutputFolder.delete(true, true, null);
      IFolder libsSourceFolder = ProjectHelper.getLibrariesFolder(project);
      
      if(!libsSourceFolder.exists())
        libsSourceFolder.create(true, true, null);
      
      libsSourceFolder.copy(libsOutputFolder.getFullPath(), true, null);
      
    } catch(Exception e) {
      MonetLog.print(e);
      MonetResourceProblemMarker.create(project, "Can't copy all resources to bin: " + e.getMessage());
    }
}

  private void generateResourcesClass(IProgressMonitor monitor, IProject project) throws CoreException {
	  
  	String packageBase = ProjectHelper.getProjectPackageBase(project);

  	IPath resourcesClassPath = this.getFilePath(project, packageBase, RESOURCES_JAVA_FILE);
  	IPath assetsClassPath = this.getFilePath(project, packageBase, ASSETS_JAVA_FILE);
	  
    ResourcesClassFileGenerator.Parameters resourcesParameters = new ResourcesClassFileGenerator.Parameters();
    resourcesParameters.packageName = packageBase;
    resourcesParameters.filePath = resourcesClassPath;
    
    AssetsClassFileGenerator.Parameters assetsParameters = new AssetsClassFileGenerator.Parameters();
    assetsParameters.packageName = packageBase;
    assetsParameters.filePath = assetsClassPath;
    
    for (Constants.RESOURCE resourceType : Constants.RESOURCE.values()) {
  		HashMap<String, String> resourceEntries = new HashMap<String, String>();
      this.getFilesFromResourceFolder(monitor, project, resourceType.toString(), resourceEntries);
      
      switch (resourceType) {
        case data:
          assetsParameters.dataResources = resourceEntries;
          break;
        case help:
          resourcesParameters.helpResources = resourceEntries;
          break;
        case images:
          assetsParameters.imageResources = resourceEntries;
          resourcesParameters.imageResources = resourceEntries;
          break;
        case templates:
          resourcesParameters.templateResources = resourceEntries;
          break;
        case layouts:
          resourcesParameters.layoutResources = resourceEntries;
          break;
      }
    }

    try {
      ResourcesClassFileGenerator rcfg = new ResourcesClassFileGenerator(monitor, resourcesParameters);
      IFile resourcesClassFile = rcfg.create();
      resourcesClassFile.setDerived(true, monitor);
      resourcesClassFile.refreshLocal(IResource.DEPTH_ONE, monitor);
      
      AssetsClassFileGenerator acfg = new AssetsClassFileGenerator(monitor, assetsParameters);
      IFile assetsClassFile = acfg.create();
      assetsClassFile.setDerived(true, monitor);
      assetsClassFile.refreshLocal(IResource.DEPTH_ONE, monitor);
    } catch (Exception e) {
      MonetLog.print(e);
      MonetResourceProblemMarker.create(project, "Can't create Resources class: " + e.getMessage());
    }
  }

	private void getFilesFromResourceFolder(IProgressMonitor monitor, IProject project, String resourceType, HashMap<String, String> resourceEntries) throws CoreException {

    IFolder resFolder = this.getFolder(project, Constants.RES_FOLDER, monitor);
    IFolder resourceTypeFolder = this.getFolder(resFolder, resourceType.toString(), monitor);

    List<IFile> resources = this.getAllFiles(resourceTypeFolder);
	  
	  for (IFile file : resources) {
	    String key = LibraryString.toJavaIdentifier(LibraryFile.getFilenameWithoutExtension(file.getName()));
	    String value = file.getFullPath().makeRelativeTo(resFolder.getFullPath()).toString();
	    resourceEntries.put(key, value);
	  }
	  
	}

  private IPath getFilePath(IProject project, String packageName, String filename) {
    final IPath containerPath = project.getFullPath().append(Constants.GEN_FOLDER);

    if (packageName != null)
      packageName = packageName.replaceAll("\\.", "/");
    else
      packageName = "";
    IPath newFilePath = containerPath.append(packageName).append(filename);
    return newFilePath;
  }

  private List<IFile> getAllFiles(IFolder folder) {
    ArrayList<IFile> files = new ArrayList<IFile>();
    this.fillWithAllFiles(files, folder);
    return files;
  }

  private void fillWithAllFiles(List<IFile> result, IFolder source) {
    try {
      for (IResource member : source.members()) {
        if (member instanceof IFile)
          result.add((IFile) member);
        else if (member instanceof IFolder)
          fillWithAllFiles(result, (IFolder) member);
      }
    } catch (Exception e) {
      MonetLog.print(e);
    }
  }


}

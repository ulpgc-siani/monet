package org.monet.editor.core.commands;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.monet.editor.Constants;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.dsl.helper.JavaHelper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryFile;
import org.monet.editor.library.LibraryFilesystem;
import org.monet.editor.library.LibraryString;
import org.monet.editor.library.StreamHelper;
import org.monet.metamodel.Manifest;

public class GeneratePackageCommand implements IJobCommand {

  private IResource project;
  private IResource distribution;
  private IResource packageResource;
  private String release;

  public GeneratePackageCommand(IResource project, IResource distribution, IResource packageResource, String release) {
    this.project = project;
    this.distribution = distribution;
    this.packageResource = packageResource;
    this.release = release;
  }
  
  @Override
  public void execute(IProgressMonitor monitor) throws Exception {
    HashMap<String, IFile> gatheredFiles = this.gatherPackageFiles(this.distribution.getProject());
    if(this.packageResource instanceof IFile)
      this.createPackage((IFile)this.packageResource, gatheredFiles, monitor);
    else if(this.packageResource instanceof IFolder)
      this.copyToDir((IFolder)this.packageResource, gatheredFiles, monitor);
  }
  
  private void copyToDir(IFolder packageFolder, HashMap<String, IFile> gatheredFiles, IProgressMonitor monitor) throws Exception {
    for(Entry<String, IFile> fileToPackage : gatheredFiles.entrySet()) {
      File packageFile = new File(packageFolder.getFile(new Path(fileToPackage.getKey())).getLocation().toString());
      LibraryFilesystem.copyFile(new File(fileToPackage.getValue().getLocation().toString()), packageFile);
    }
    
    InputStream input = null;
    try {
      input = this.buildProjectStream();
      LibraryFilesystem.writeContents(new File(packageFolder.getFile("MANIFEST").getLocation().toString()), input);
    } finally {
      StreamHelper.close(input);
    }
  }

  private void createPackage(IFile packageFile, HashMap<String, IFile> filesToPackage, IProgressMonitor monitor) throws Exception {
    ZipOutputStream zip = null;
    FileInputStream tempFileStream = null;
    File tempFile = File.createTempFile("package", "");
    
    try {
      zip = new ZipOutputStream(new FileOutputStream(tempFile));
      
      for(Entry<String, IFile> fileToPackage : filesToPackage.entrySet()) {
        ZipEntry entry = new ZipEntry(fileToPackage.getKey());
        zip.putNextEntry(entry);

        InputStream input = null;
        try {
          if(!fileToPackage.getValue().isSynchronized(IFile.DEPTH_ZERO))
            fileToPackage.getValue().refreshLocal(IFile.DEPTH_ZERO, null);
          
          input = fileToPackage.getValue().getContents();
          StreamHelper.copyData(input, zip);
        } finally {
          StreamHelper.close(input);
        }
      }
      
      InputStream input = null;
      try {
        ZipEntry manifestEntry = new ZipEntry("MANIFEST");
        zip.putNextEntry(manifestEntry);
        input = this.buildProjectStream();
        StreamHelper.copyData(input, zip);
      } finally {
        StreamHelper.close(input);
      }
    } finally {
      StreamHelper.close(zip);
    }
    
    try {
      tempFileStream = new FileInputStream(tempFile);
      EclipseHelper.createFile(packageFile, tempFileStream, monitor);
    } finally {
      StreamHelper.close(tempFileStream);
    }
    tempFile.delete();
  }
  
  //Devuelve un mapa, indexado por ruta en el ZIP final, con los archivos a empaquetar
  private HashMap<String, IFile> gatherPackageFiles(IProject project) throws CoreException {
    HashMap<String, IFile> gatheredFiles = new HashMap<String, IFile>();
    String projectName = JavaHelper.toJavaIdentifier(this.project.getProject().getName());
    String distributionName = JavaHelper.toJavaIdentifier(this.distribution.getParent().getName());
    
    //Añadir ficheros sin incluir el manifest
    this.addProjectFiles(this.distribution.getProject(), gatheredFiles);
    
    String manifestPackage = ProjectHelper.getProjectPackageBase(project).replaceAll("\\.", "/") + "/" + Constants.MANIFEST_PACKAGE;
    Path manifestPackagePath = new Path(Constants.JAVA_OUT_FOLDER + "/" + manifestPackage);
    
    //Añadir manifest del proyecto concreto
    String manifestClassName = LibraryString.toJavaIdentifier(LibraryFile.getFilenameWithoutExtension(this.project.getName()) + projectName) + ".class";
    IFile manifestClassFile = project.getFile(manifestPackagePath.append(manifestClassName));
    gatheredFiles.put(manifestClassFile.getProjectRelativePath().removeFirstSegments(1).toString(), manifestClassFile);
    
    //Añadir distribution concreto
    String distributionClassName = LibraryString.toJavaIdentifier(LibraryFile.getFilenameWithoutExtension(this.distribution.getName()) + distributionName) + ".class";
    IFile distributionClassFile = project.getFile(manifestPackagePath.append(distributionClassName));
    gatheredFiles.put(distributionClassFile.getProjectRelativePath().removeFirstSegments(1).toString(), distributionClassFile);
    
    //Añadir recursos concretos del setup
    this.projectFileExplorer(this.distribution.getParent(), gatheredFiles, null, 2);
    gatheredFiles.remove(Constants.DISTRIBUTION_FILE);
    gatheredFiles.remove(Constants.DISTRIBUTION_DISABLED_FILE);
    
    return gatheredFiles;
  }
  
  private void addProjectFiles(IProject project, HashMap<String, IFile> gatheredFiles) throws CoreException {
    
    String[] manifestPackageFolder = ProjectHelper.getProjectPackageBase(project).split("\\.");
    this.projectFileExplorer(ProjectHelper.getBinFolder(project), gatheredFiles, manifestPackageFolder, 1);
  }
  
  private void projectFileExplorer(IContainer container, HashMap<String, IFile> gatheredFiles, String[] manifestPackageFolder, int distanceFromRoot) throws CoreException {
    for(IResource resource : container.members()) {
      if(resource instanceof IFolder) {
        boolean isNotManifestFolder = true;
        if(resource.getName().equals(Constants.MANIFEST_PACKAGE) && 
           (resource.getProjectRelativePath().segmentCount() - 2) == (manifestPackageFolder.length + 1)) {
          int i = 0;
          isNotManifestFolder = false;
          IPath folderPath = resource.getProjectRelativePath().removeFirstSegments(2).removeLastSegments(1);
          for(String segment : folderPath.segments()) {
            if(!segment.equals(manifestPackageFolder[i])) {
              isNotManifestFolder = true;
              break;
            }
            i++;
          }
        }
        if(isNotManifestFolder) {
          this.projectFileExplorer((IFolder)resource, gatheredFiles, manifestPackageFolder, distanceFromRoot);
        }
      } else if(resource instanceof IFile) {
        IFile file = (IFile)resource;
        if(file.getFileExtension().equals(Constants.MML_FILE_EXTENSION))
          continue;
        String modelPath = file.getProjectRelativePath().removeFirstSegments(distanceFromRoot).toString();
        gatheredFiles.put(modelPath, file);
      }
    }
  }
  
  private InputStream buildProjectStream() throws UnsupportedEncodingException {
    StringBuilder builder = new StringBuilder();
    IProject projectInstance = this.project.getProject();
    builder.append("Version: ");
    builder.append(Manifest.getVersion());
    builder.append("\nRelease: ");
    builder.append(this.release);
    builder.append("\nUUID: ");
    builder.append(ProjectHelper.getProjectUUID(projectInstance));
    builder.append("\nProject: ");
    builder.append(ProjectHelper.getProjectPackageBase(projectInstance));
    builder.append(".manifest.Project");
    builder.append(JavaHelper.toJavaIdentifier(projectInstance.getName()));
    builder.append("\nDistribution: ");
    builder.append(ProjectHelper.getProjectPackageBase(projectInstance));
    builder.append(".manifest.Distribution");
    builder.append(JavaHelper.toJavaIdentifier(this.distribution.getParent().getName()));
    ByteArrayInputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
    return inputStream;
  }
  
  @Override
  public boolean canExecute() {
    return true;
  }
  
}

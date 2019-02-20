package org.monet.editor.core.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.monet.editor.Constants;
import org.monet.editor.Constants.RESOURCE;
import org.monet.editor.MonetPreferences;
import org.monet.editor.core.generators.DistributionFileGenerator;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.core.wrappers.ProjectWrapper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryFile;
import org.monet.editor.library.LibraryFilesystem;
import org.monet.editor.library.StreamHelper;

public class CreateDistributionCommand implements IJobCommand {

  public static class Parameters {

    public IProject project;
    public String   name;

  }

  private Parameters parameters;

  public CreateDistributionCommand(Parameters parameters) {
    this.parameters = parameters;
  }

  public void execute(IProgressMonitor monitor) throws Exception {
    monitor.beginTask("Working...", 0);

    IFolder distFolder = ProjectHelper.getDistFolder(this.parameters.project);

    IFolder distributionFolder = distFolder.getFolder(this.parameters.name);
    EclipseHelper.createFolder(distributionFolder, monitor);

    DistributionFileGenerator.Parameters parameters = new DistributionFileGenerator.Parameters();

    ProjectWrapper projectModel = ProjectWrapper.fromFile(ProjectHelper.getProjectFile(this.parameters.project));

    parameters.project = projectModel.getName();
    parameters.name = this.parameters.name;
    parameters.outputFile = distributionFolder.getFile(Constants.DISTRIBUTION_FILE);

    DistributionFileGenerator sfg = new DistributionFileGenerator(monitor, parameters);
    sfg.create();

    // Create folders
    IFolder resFolder = distributionFolder.getFolder(Constants.RES_FOLDER);
    EclipseHelper.createFolder(resFolder, monitor);

    for (RESOURCE foldername : Constants.RESOURCE.values()) {
      EclipseHelper.createFolder(resFolder.getFolder(foldername.toString()), monitor);
    }
    
    processTemplate(distributionFolder, monitor);

    monitor.done();
  }

  protected void processTemplate(IFolder basePath, IProgressMonitor monitor) throws Exception {
    List<File> files = new ArrayList<File>();
    LibraryFilesystem.listFiles(MonetPreferences.getTemplateDistributionsPath(), files, true);

    for (File file : files) {
      this.createTemplate(MonetPreferences.getTemplateDistributionsPath(), file.getAbsolutePath(), basePath, monitor);
    }
  }

  protected IFile createTemplate(String parentFolder, String templateName, IContainer containerFolder, IProgressMonitor monitor) throws Exception {
    String filename = LibraryFile.getFilename(templateName);
    String foldersPath = LibraryFile.getDirname(templateName.substring(parentFolder.length() + 1));
    IFile newFile = containerFolder.getFile(new Path(new File(foldersPath, filename).getPath()));

    File templateFile = new File(templateName);
    InputStream templateStream = null;
    try {
      templateStream = new FileInputStream(templateFile);
      EclipseHelper.createFile(newFile, templateStream, null);
    } finally {
      StreamHelper.close(templateStream);
    }

    return newFile;
  }

  @Override
  public boolean canExecute() {
    return true;
  }

}

package org.monet.editor.core.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.monet.editor.Constants;
import org.monet.editor.MonetPreferences;
import org.monet.editor.core.classpath.MonetContainer;
import org.monet.editor.core.generators.ProjectFileGenerator;
import org.monet.editor.core.generators.PatternFileGenerator;
import org.monet.editor.core.generators.ProjectPropertiesFileGenerator;
import org.monet.editor.core.generators.UserPropertiesFileGenerator;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryFile;
import org.monet.editor.library.LibraryFilesystem;
import org.monet.editor.library.StreamHelper;

public class CreateProjectCommand implements IJobCommand {

	public static class Parameters {
		public String name;
		public String label;
		public String basePackage;
		public String template;
		public String parent;
	}

	private Parameters parameters;

	public CreateProjectCommand(Parameters parameters) {
		this.parameters = parameters;
	}

	public void execute(IProgressMonitor monitor) throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();

		IProjectDescription projectDescription = this.createProjectDescription(workspace, this.parameters.name);

		IProject parentProject = this.getParentProject(workspaceRoot);

		IProject newProject = this.createProject(monitor, workspaceRoot, projectDescription);

		this.createJavaProject(monitor, newProject);

		ProjectHelper.getBinFolder(newProject).delete(true, true, null);
		ProjectHelper.getBinFolder(newProject).create(true, true, null);

		if (parentProject == null) {
			this.copyContentFromTemplate(newProject, monitor); 
		} else {
			this.copyContentFromParent(parentProject, newProject, monitor);	
		}

		newProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private IJavaProject createJavaProject(IProgressMonitor monitor, IProject newProject) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(newProject);
		if (javaProject.exists() && !javaProject.isOpen()) {
			javaProject.open(monitor);
		}

		javaProject.setOutputLocation(newProject.getFolder(new Path(Constants.JAVA_OUT_FOLDER)).getFullPath(), monitor);

		this.setupJavaProjectClasspath(monitor, newProject, javaProject);
		
		return javaProject;
	}

	private void setupJavaProjectClasspath(IProgressMonitor monitor, IProject newProject, IJavaProject javaProject) throws JavaModelException {
		Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
		entries.add(JavaCore.newSourceEntry(newProject.getFolder(new Path(Constants.SOURCE_FOLDER)).getFullPath()));
		entries.add(JavaCore.newSourceEntry(newProject.getFolder(new Path(Constants.GEN_FOLDER)).getFullPath()));
		entries.add(JavaRuntime.getDefaultJREContainerEntry());
		entries.add(JavaCore.newContainerEntry(new Path("org.eclipse.xtend.XTEND_CONTAINER")));
		entries.add(JavaCore.newContainerEntry(MonetContainer.ID));

		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), monitor);
	}

	private IProject createProject(IProgressMonitor monitor, IWorkspaceRoot workspaceRoot, IProjectDescription projectDescription) throws Exception {
		IProject newProject = workspaceRoot.getProject(this.parameters.name);

		newProject.create(projectDescription, monitor);
		if (!newProject.isOpen()) {
			newProject.open(monitor);
		}

		newProject.setDefaultCharset("UTF-8", monitor);
		
		for (String foldername : Constants.PROJECT_FOLDERS) {
			EclipseHelper.createFolder(newProject.getFolder(foldername), monitor);
		}
		
		this.createInitialContent(newProject, monitor);
		
		return newProject;
	}

	private IProjectDescription createProjectDescription(IWorkspace workspace, String projectName) {
		IProjectDescription projectDescription = workspace.newProjectDescription(projectName);
		
		projectDescription.setNatureIds(new String[] { Constants.MONET_NATURE_ID, JavaCore.NATURE_ID, Constants.XTEXT_NATURE_ID });
		
		projectDescription.setBuildSpec(new org.eclipse.core.resources.ICommand[] {
				createBuilder(projectDescription, Constants.RESOURCE_BUILDER_ID),
				createBuilder(projectDescription, JavaCore.BUILDER_ID),
				createBuilder(projectDescription, Constants.XTEXT_BUILDER_ID) });
		
		return projectDescription;
	}

	private IProject getParentProject(IWorkspaceRoot workspaceRoot) {
		IProject parentProject = null;

		if ((this.parameters.parent != null) && !this.parameters.parent.isEmpty()) {
			parentProject = workspaceRoot.getProject(this.parameters.parent);
		}
		return parentProject;
	}

	private void createInitialContent(IProject project, IProgressMonitor monitor) throws Exception {
		ProjectPropertiesFileGenerator.Parameters propertiesParams = new ProjectPropertiesFileGenerator.Parameters();
		propertiesParams.packageName = this.parameters.basePackage;
		propertiesParams.release = "1";
		propertiesParams.parent = this.parameters.parent;
		ProjectPropertiesFileGenerator pfg = new ProjectPropertiesFileGenerator(project, monitor, propertiesParams);
		pfg.create();

		UserPropertiesFileGenerator.Parameters userPropertiesParams = new UserPropertiesFileGenerator.Parameters();
		userPropertiesParams.path = new File(MonetPreferences.getHomePath(), Constants.PRIVATE_KEY_TEMPLATE_NAME).getAbsolutePath();
		UserPropertiesFileGenerator upg = new UserPropertiesFileGenerator(project, monitor, userPropertiesParams);
		upg.create();

		ProjectFileGenerator.Parameters projectParams = new ProjectFileGenerator.Parameters();
		projectParams.author = System.getProperty("user.name");
		projectParams.name = this.parameters.name;
		ProjectFileGenerator mfg = new ProjectFileGenerator(project, monitor, projectParams);
		mfg.create();
	}

	private void copyContentFromTemplate(IProject newProject, IProgressMonitor monitor) throws Exception {
		String template = this.parameters.template;
		if ((template == null) || template.isEmpty()) return;
			
		List<File> files = new ArrayList<File>();
		LibraryFilesystem.listFiles(this.parameters.template, files, true);
		this.addFilesToProject(newProject, files, this.parameters.template, monitor);
	}
	
	private void copyContentFromParent(IProject parentProject, IProject newProject, IProgressMonitor monitor) throws Exception {
		if (parentProject == null) return;
		
		String parentProjectLocation = parentProject.getLocation().toOSString();
		
		List<File> files = new ArrayList<File>();

		String sourcePath = parentProject.getFolder(Constants.SOURCE_FOLDER).getLocation().toOSString();
		LibraryFilesystem.listFiles(sourcePath, files, true);

		sourcePath = parentProject.getFolder(Constants.RES_FOLDER).getLocation().toOSString();
		LibraryFilesystem.listFiles(sourcePath, files, true);

		sourcePath = parentProject.getFolder(Constants.LIBS_FOLDER).getLocation().toOSString();
		LibraryFilesystem.listFiles(sourcePath, files, true);

		this.addFilesToProject(newProject, files, parentProjectLocation, monitor);
	}
	
	private void addFilesToProject(IProject newProject, List<File> files, String sourcePath, IProgressMonitor monitor) throws Exception {

		for (File file : files) {
			this.addFileToProject(newProject, file, sourcePath, monitor);
		}
	}

	private IFile addFileToProject(IProject newProject, File sourceFile, String sourcePath, IProgressMonitor monitor) throws Exception {

		String relativePath = LibraryFile.getDirname(sourceFile.getAbsolutePath().substring(sourcePath.length() + 1));

		if (relativePath != null) {
			relativePath = relativePath.replace("$base-package", this.parameters.basePackage.replaceAll("\\.", "/"));
			IFolder folder = newProject.getFolder(new Path(relativePath));
			EclipseHelper.createFolder(folder, monitor);
			relativePath += File.separator;
		} else {
			relativePath = "";
		}

		IFile newFile = newProject.getFile(new Path(relativePath + sourceFile.getName()));

		if (sourceFile.getName().endsWith(Constants.MML_FILE_EXTENSION)) {
			doCopyFileWithReplacements(sourceFile, newProject, newFile, monitor);
		} else {
			doCopyFileDirect(sourceFile, newFile);
		}

		return newFile;
	}

	private void doCopyFileWithReplacements(File sourceFile, IProject newProject, IFile newFile, IProgressMonitor monitor) throws Exception {
		PatternFileGenerator.Parameters patternParameters = new PatternFileGenerator.Parameters();
		patternParameters.project = newProject.getName();
		patternParameters.basePackage = this.parameters.basePackage;
		patternParameters.patternFilename = sourceFile.getName();
		patternParameters.patternPath = LibraryFile.getDirname(sourceFile.getAbsolutePath());
		patternParameters.outputFile = newFile;
		PatternFileGenerator pfg = new PatternFileGenerator(monitor, patternParameters);
		pfg.create();
	}

	private void doCopyFileDirect(File sourceFile, IFile newFile) throws FileNotFoundException, Exception {
		InputStream sourceStream = null;
		try {
			sourceStream = new FileInputStream(sourceFile);
			EclipseHelper.createFile(newFile, sourceStream, null);
		} finally {
			StreamHelper.close(sourceStream);
		}
	}

	private org.eclipse.core.resources.ICommand createBuilder(IProjectDescription desc, String builderId) {
		org.eclipse.core.resources.ICommand builderCommand = desc.newCommand();
		builderCommand.setBuilderName(builderId);
		return builderCommand;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

}

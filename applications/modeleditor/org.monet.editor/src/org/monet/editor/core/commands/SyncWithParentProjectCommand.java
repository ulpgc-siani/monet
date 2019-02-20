package org.monet.editor.core.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ProjectHelper;
import org.monet.editor.diffmatcpatch.DiffLineMode;
import org.monet.editor.library.EclipseHelper;
import org.monet.editor.library.LibraryFile;
import org.monet.editor.library.LibraryFilesystem;
import org.monet.editor.library.StreamHelper;

public class SyncWithParentProjectCommand implements IJobCommand {

	public static final String LINE_COMMENT = "//";
	public static final String BLOCK_COMMENT = "//MERGE";
	
	public static class Parameters {
		public IProject project;
	}	
	
	private IProgressMonitor monitor;
	
	private IProject currentProject;
	private IProject parentProject;
  
	public SyncWithParentProjectCommand(Parameters parameters) {
		super();
		this.currentProject = parameters.project;
	}

	@Override
	public boolean canExecute() {
		try {
			return getParentProject() != null;
		} catch(Exception e) {
			return false;
		}
	}
  
	@Override
	public void execute(IProgressMonitor monitor) {
		this.monitor = monitor;
		this.monitor.beginTask("Working...", 0);

		try {
			this.parentProject = this.getParentProject();
			if (this.parentProject == null) return;

			this.syncLibsFolder(monitor);
			this.syncResFolder(monitor);
			this.syncSourceFolder(monitor);
    	
			this.currentProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (Exception e) {
			MonetLog.print(e);
			EclipseHelper.showErrorMessage("Error executing the operation. See Error Log for more details.");
		} finally {
		}
		this.monitor.done();
	}
	
	private IProject getParentProject() throws CoreException {
		IProject parent = ProjectHelper.getParentProject(this.currentProject);
		if ((parent == null) || (!parent.exists()))
			return null;
		
		return parent;
	}

	private void syncSourceFolder(IProgressMonitor monitor) throws Exception {
		IFolder parentFolder = ProjectHelper.getSourceFolder(this.parentProject);
		IFolder currentFolder = ProjectHelper.getSourceFolder(this.currentProject);
		
		List<File> files = new ArrayList<File>();
		LibraryFilesystem.listFiles(parentFolder.getLocation().toOSString(), files, true);
		
		this.mergeFilesToFolder(currentFolder, files, parentFolder.getLocation().toOSString(), monitor);
  }

	private void syncResFolder(IProgressMonitor monitor) throws Exception {
		IFolder parentFolder = ProjectHelper.getResourcesFolder(this.parentProject);
		IFolder currentFolder = ProjectHelper.getResourcesFolder(this.currentProject);
		
		List<File> files = new ArrayList<File>();
		LibraryFilesystem.listFiles(parentFolder.getLocation().toOSString(), files, true);
		
		this.addFilesToFolder(currentFolder, files, parentFolder.getLocation().toOSString(), monitor);
  }

	private void syncLibsFolder(IProgressMonitor monitor) throws Exception {
		IFolder parentFolder = ProjectHelper.getLibrariesFolder(this.parentProject);
		IFolder currentFolder = ProjectHelper.getLibrariesFolder(this.currentProject);
		
		List<File> files = new ArrayList<File>();
		LibraryFilesystem.listFiles(parentFolder.getLocation().toOSString(), files, true);
		
		this.addFilesToFolder(currentFolder, files, parentFolder.getLocation().toOSString(), monitor);
  }

	private void addFilesToFolder(IFolder currentFolder, List<File> files, String sourcePath, IProgressMonitor monitor) throws Exception {
		for (File file : files) {
			this.addFileToFolder(currentFolder, file, sourcePath, monitor);
		}
  }
	
	private void addFileToFolder(IFolder currentFolder, File sourceFile, String sourcePath, IProgressMonitor monitor) throws Exception {

		String relativePath = LibraryFile.getDirname(sourceFile.getAbsolutePath().substring(sourcePath.length() + 1));

		if (relativePath != null) {
			IFolder folder = currentFolder.getFolder(new Path(relativePath));
			if (!folder.exists())
				EclipseHelper.createFolder(folder, monitor);
			relativePath += File.separator;
		} else {
			relativePath = "";
		}

		IFile destFile = currentFolder.getFile(new Path(relativePath + sourceFile.getName()));
		if (!destFile.exists())
			this.doCopyFileDirect(sourceFile, destFile);
	}	

	private void mergeFilesToFolder(IFolder currentFolder, List<File> files, String sourcePath, IProgressMonitor monitor2) throws Exception {
		for (File file : files) {
			this.mergeFileToFolder(currentFolder, file, sourcePath, monitor);
		}
  }
	
	private void mergeFileToFolder(IFolder currentFolder, File sourceFile, String sourcePath, IProgressMonitor monitor) throws Exception {

		String relativePath = LibraryFile.getDirname(sourceFile.getAbsolutePath().substring(sourcePath.length() + 1));

		if (relativePath != null) {
			IFolder folder = currentFolder.getFolder(new Path(relativePath));
			if (!folder.exists())
				EclipseHelper.createFolder(folder, monitor);
			relativePath += File.separator;
		} else {
			relativePath = "";
		}

		IFile destFile = currentFolder.getFile(new Path(relativePath + sourceFile.getName()));
		if (!destFile.exists()) {
			this.doCopyFileDirect(sourceFile, destFile);
		} else {
			this.merge(destFile.getLocation().toFile(), sourceFile);
		}
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

	private void merge(File currentFile, File sourceFile) throws CoreException {
		DiffLineMode diffLineMode = new DiffLineMode();
		try {
			diffLineMode.setDeleteLineMark(LINE_COMMENT);
			diffLineMode.setDeleteBlockMark(BLOCK_COMMENT);
			
			File tempFile = File.createTempFile("merge", "");
			try {
				diffLineMode.execute(sourceFile, currentFile, tempFile);

				this.copyFile(tempFile, currentFile);
			} finally {
				tempFile.delete();
			}
		} catch (Exception e) {
			MonetLog.print("No se ha podido realizar el merge de los ficheros " + sourceFile.getAbsolutePath() + " y " + currentFile.getAbsolutePath(), e);
			EclipseHelper.showErrorMessage("Error executing the operation. See Error Log for more details.");		
		}
	}
	
	
	private void copyFile(File sourceFile, File destFile) throws IOException{
		InputStream in = new FileInputStream(sourceFile);
		OutputStream out = new FileOutputStream(destFile);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

}	



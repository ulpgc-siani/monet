package org.monet.editor.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ProjectHelper;

public class MonetContainer implements IClasspathContainer {

  public final static Path   ID          = new Path("org.monet.editor.MONET_CONTAINER");

  public final static String DESCRIPTION = "Monet Libraries";

  private IPath              path;
  private IJavaProject       javaProject;

  public MonetContainer(IPath containerPath, IJavaProject javaProject) {
    this.path = containerPath;
    this.javaProject = javaProject;
  }

  @Override
  public IClasspathEntry[] getClasspathEntries() {
    ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();

    try {
      String bpiLibraryPath = org.monet.Activator.getBundlePath();
      entryList.add(JavaCore.newLibraryEntry(new Path(bpiLibraryPath), null, new Path("/")));
      
      IProject project = this.javaProject.getProject();
      
      for(IResource lib : ProjectHelper.getLibrariesFolder(project).members()) {
        if("jar".equalsIgnoreCase(lib.getFileExtension()))
            entryList.add(JavaCore.newLibraryEntry(lib.getFullPath(), null, new Path("/")));
      }
    } catch (Exception e) {
      MonetLog.print(e);
    }

    IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
    return (IClasspathEntry[]) entryList.toArray(entryArray);
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public int getKind() {
    return IClasspathContainer.K_APPLICATION;
  }

  @Override
  public IPath getPath() {
    return this.path;
  }

}

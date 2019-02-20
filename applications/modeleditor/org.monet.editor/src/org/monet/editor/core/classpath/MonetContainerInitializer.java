package org.monet.editor.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class MonetContainerInitializer extends org.eclipse.jdt.core.ClasspathContainerInitializer {

  @Override
  public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
    MonetContainer container = new MonetContainer(containerPath, project);
    JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container }, null);
  }

}

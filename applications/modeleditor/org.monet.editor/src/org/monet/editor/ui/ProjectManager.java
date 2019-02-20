package org.monet.editor.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.monet.editor.Constants;

public class ProjectManager {
  private static ProjectManager instance;
  private IWorkbenchWindow      window;

  public static ProjectManager instance() {
    if (instance == null)
      instance = new ProjectManager();
    return instance;
  }

  private ProjectManager() {
    this.window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  public IProject getActiveProject() {
    return getActiveProject(null);
  }

  public IProject getActiveProjectFromModelExplorer() {
      IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection(Constants.ID_MODEL_EXPLORER_VIEW);
      IProject project = getProjectFromSelection(selection);
      return project;
  }
  
  public IProject getActiveProject(IStructuredSelection selection) {
    IProject project = null;

    project = getProjectFromSelection(selection);

    if (project == null)
      project = getActiveProjectFromEditor();

    if (project == null) {
    	project = this.getActiveProjectFromModelExplorer();
    }

    return project;
  }

  IProject getProjectFromSelection(IStructuredSelection selection) {
    IProject project = null;
    if (selection != null && !selection.isEmpty()) {
      Object firstElement = selection.getFirstElement();
      if (firstElement instanceof IPackageFragment) {
        project = ((IPackageFragment) firstElement).getJavaProject().getProject();
      } else if (firstElement instanceof IProject) {
        project = (IProject) firstElement;
      } else if (firstElement instanceof IResource) {
        project = ((IResource) firstElement).getProject();
      }
    }
    return project;
  }

  private IProject getActiveProjectFromEditor() {
    IEditorPart editorPart = window.getActivePage().getActiveEditor();
    IProject project = null;
    if (editorPart != null) {
      IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
      IFile file = input.getFile();
      project = file.getProject();
    }
    return project;
  }

}

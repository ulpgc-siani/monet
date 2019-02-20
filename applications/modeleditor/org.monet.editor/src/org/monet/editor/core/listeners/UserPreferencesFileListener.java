package org.monet.editor.core.listeners;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ProjectHelper;

public class UserPreferencesFileListener implements IResourceChangeListener {

  public static final void init() {
    ResourcesPlugin.getWorkspace().addResourceChangeListener(new UserPreferencesFileListener());
  }
  
  @Override
  public void resourceChanged(IResourceChangeEvent event) {
    try {
      event.getDelta().accept(new IResourceDeltaVisitor() {

        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
          processDelta(delta);
          return false;
        }

        private void processDelta(IResourceDelta delta) {
          if (delta == null)
            return;
          IResourceDelta[] childs = delta.getAffectedChildren();
          for (IResourceDelta child : childs) {
            if (child.getResource() instanceof IContainer)
              processDelta(child);
            else {
              IResource resource = child.getResource();
              if (resource instanceof IFile && resource.getName().equals(Constants.USER_PREFERENCES_FILENAME)) {
                ProjectHelper.invalidateUserPreferences(resource.getProject());
              }
            }
          }
        }
      });
    } catch (CoreException e) {
      MonetLog.print(e);
    }
  }

}

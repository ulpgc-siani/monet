package org.monet.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class MonetRuntimeProblemMarker {

  public static final String ID = "org.monet.editor.runtimeproblemmarker";

  public static void create(IResource target, String message) throws CoreException {
    IMarker marker = target.createMarker(ID);
    marker.setAttribute(IMarker.MESSAGE, message);
    marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
  }
  
  public static void clean(IResource target) throws CoreException {
    target.deleteMarkers(ID, true, IResource.DEPTH_INFINITE);
  }

}

package org.monet.editor.core.builders.errors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;

public class Error extends Problem {

	public Error(IResource resource, String message, int numberLine) {
		super(resource, message,numberLine, IMarker.SEVERITY_ERROR);
	}
  
}

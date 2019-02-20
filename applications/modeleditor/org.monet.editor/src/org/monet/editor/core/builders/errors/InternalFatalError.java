package org.monet.editor.core.builders.errors;

import org.eclipse.core.resources.IResource;

public class InternalFatalError extends Error {

	public InternalFatalError(IResource resource, String message) {
		super(resource, String.format("Internal error: %s", message), 1);
	}
}

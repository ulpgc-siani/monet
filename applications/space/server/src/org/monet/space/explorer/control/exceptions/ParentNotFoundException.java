package org.monet.space.explorer.control.exceptions;

public class ParentNotFoundException extends RuntimeException {
	private final String message;

	public ParentNotFoundException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "error-parent-not-found:" + message;
	}

}

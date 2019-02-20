package org.monet.grided.exceptions;


public class FilesystemException extends SystemException {

    private static final long serialVersionUID = 1L;

	public FilesystemException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FilesystemException(String message) {
		super(message);
	}

}

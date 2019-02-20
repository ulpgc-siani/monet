package org.monet.editor.library;


public class FileSystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public FileSystemException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FileSystemException(String message) {
		super(message);
	}

}

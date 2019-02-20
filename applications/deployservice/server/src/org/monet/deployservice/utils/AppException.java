package org.monet.deployservice.utils;

public class AppException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int errorCode;
	
	public AppException() {
		super();
	}
	
	public AppException(int errorCode) {
		super();
		this.errorCode= errorCode;
	}
	
	public AppException(String message) {
		super(message);		
	}
		
	public AppException(int errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}
	
	public AppException(String message, Throwable throwable) {
		super(message, throwable);
	}	

	public AppException(String message, int errorCode, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}	

	public AppException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}
}

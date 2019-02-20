package org.monet.grided.exceptions;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorCode;
    private String message;

	public BaseException() {
		super();
	}

	public BaseException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public BaseException(Throwable throwable) {
	  super(throwable);
	}
	
	public BaseException(String errorCode, String message) {
	    this.errorCode = errorCode;
	    this.message = message;	            
	}

	public BaseException(String errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

    public String getErrorCode() {
		return this.errorCode;
	}
	
	public String getMessage() {
	    return this.message;
	}
}

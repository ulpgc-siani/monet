package org.monet.grided.exceptions;

public class ConfigurationException extends SystemException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}
	
	public ConfigurationException(String errorCode) {
		super(errorCode);	
	}
	
	public ConfigurationException(String errorCode, String message) {
        super(errorCode, message);   
    }    

	public ConfigurationException(String errorCode, Throwable throwable) {
		super(errorCode, throwable);		
	}
}


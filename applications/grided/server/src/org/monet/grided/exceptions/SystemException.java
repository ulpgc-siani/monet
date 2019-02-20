package org.monet.grided.exceptions;


public class SystemException extends BaseException {

    private static final long serialVersionUID = 1L;

    public SystemException() {
        super();
    }
    
    public SystemException(String errorCode) {
        super(errorCode);   
    }
    
    public SystemException(String errorCode, String message) {
        super(errorCode, message);   
    }    

    public SystemException(String errorCode, Throwable throwable) {
        super(errorCode, throwable);        
    }
    
    public SystemException(Throwable throwable) {
        super(throwable);        
    }
}

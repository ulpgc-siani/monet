package org.monet.grided.exceptions;

public class DataStoreException extends BaseException {

    private static final long serialVersionUID = 1L;
    private String filename;

    public DataStoreException(String errorCode, Throwable throwable) {
        super(errorCode, throwable);        
    }
    
    public DataStoreException(String errorCode, String filename) {
        super(errorCode);
        this.filename = filename;
    }

    public DataStoreException(String errorCode, Throwable throwable, String filename) {
        super(errorCode, throwable);        
        this.filename = filename;
    }
    
    public String getFilename() {
        return this.filename;
    }
}


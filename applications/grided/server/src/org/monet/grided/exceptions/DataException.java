package org.monet.grided.exceptions;

public class DataException extends BaseException {

    private static final long serialVersionUID = 1L;
    private String query;
        
    public DataException(String errorCode, Throwable throwable) {
        super(errorCode, throwable);        
    }

    public DataException(String errorCode, Throwable throwable, String query) {
        super(errorCode, throwable);        
        this.query = query;
    }
    
    public String getQuery() {
        return this.query;
    }
}


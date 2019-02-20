package org.monet.docservice.core.exceptions;

public class DocServiceException extends RuntimeException {

  private static final long serialVersionUID = 6350039618245271071L;

  public DocServiceException(String message) {
    super(message);
  }
  
  public DocServiceException(String message, Exception exception) {
    super(message, exception);
  }
  
}

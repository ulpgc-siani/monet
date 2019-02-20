package org.monet.exceptions;

public class ApplicationException extends BaseException {
  private static final long serialVersionUID = 1L;

  public ApplicationException(String msg, Exception oReason) {
    super(msg, oReason);
  }
  
  public ApplicationException(String msg) {
    super(msg);
  }
  
}
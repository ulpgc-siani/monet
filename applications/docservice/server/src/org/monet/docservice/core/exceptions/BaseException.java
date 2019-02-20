package org.monet.docservice.core.exceptions;

public class BaseException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private String sErrorMsg;
  private Exception oReason;
  
  public BaseException(String msg) {
    this.sErrorMsg = msg;
    this.oReason = null;
  }

  public BaseException(String msg, Exception oReason) {
    super(oReason);
    this.sErrorMsg = msg;
    this.oReason = oReason;
  }
  
  public Exception getReason() {
    return this.oReason;
  }

  public String getMessage() {
    return this.sErrorMsg;
  }

}
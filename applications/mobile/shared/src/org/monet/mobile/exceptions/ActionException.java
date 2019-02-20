package org.monet.mobile.exceptions;

import org.monet.mobile.service.ErrorResult;

public class ActionException extends Exception {

  private static final long serialVersionUID = -1650569279698434165L;
  
  private ErrorResult errorResult;

  public ActionException(ErrorResult errorResult) {
    super(errorResult.toString());
    this.errorResult = errorResult;
  }

  public ErrorResult getErrorResult() {
    return errorResult;
  }
}

package org.monet.docservice.applet;

public class OperationException extends Exception {

  private static final long serialVersionUID = -5398080534354716976L;

  public OperationException(String operation, Exception innerException) {
    super(String.format("Error in operation %s", operation), innerException);
  }
  
}

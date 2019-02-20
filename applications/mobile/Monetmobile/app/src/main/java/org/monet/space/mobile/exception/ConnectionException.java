package org.monet.space.mobile.exception;

public class ConnectionException extends Exception {

  private static final long serialVersionUID = -6584019212823844356L;

  public ConnectionException(String message) {
    super(message);
  }

  public ConnectionException(String message, Exception e) {
    super(message, e);
  }

}

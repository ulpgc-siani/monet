package org.monet.space.mobile.exception;

public class SerializeException extends Exception {

  private static final long serialVersionUID = 7361552931684994515L;

  public SerializeException(String message) {
    super(message);
  }

  public SerializeException(String message, Exception e) {
    super(message, e);
  }

}

package org.monet.exceptions;

public class ConfigurationException extends BaseException {
	
  private static final long serialVersionUID = 2748698221305737789L;

  public ConfigurationException(String msg) {
    super(msg);
  }
  
  public ConfigurationException(String msg, Exception cause) {
		super(msg, cause);
	}
}

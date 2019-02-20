package org.monet.exceptions;

public class ParameterNotFoundInQueryException extends RuntimeException {

  private static final long serialVersionUID = 5626738644618250023L;

  public ParameterNotFoundInQueryException(String name) {
    super(String.format("Parameter %s can't be found in the query", name));
  }
  
}

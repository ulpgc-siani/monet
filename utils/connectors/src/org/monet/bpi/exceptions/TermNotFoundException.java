package org.monet.bpi.exceptions;

import org.monet.bpi.BPIField;

public class TermNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5625114889350557456L;

  private BPIField<?> bpiField;
  
  public TermNotFoundException(BPIField<?> bpiField) {
    this.bpiField = bpiField;
  }
  
  public BPIField<?> getField() {
    return this.bpiField;
  }
  
}

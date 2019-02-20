package org.monet.mobile.service;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root(name="error")
@Default(DefaultType.PROPERTY)
public abstract class ErrorResult {

  public ErrorResult() {
    
  }

}

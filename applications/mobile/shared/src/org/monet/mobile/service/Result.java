package org.monet.mobile.service;

import org.monet.mobile.service.results.NullResult;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root(name="result")
@Default(value = DefaultType.FIELD, required = false)
public abstract class Result {

  public static NullResult NULL = new NullResult();
  
  public Result() {
    
  }

}

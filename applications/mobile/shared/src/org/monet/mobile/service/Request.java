package org.monet.mobile.service;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root(name="request")
@Default(value = DefaultType.FIELD, required = false)
public abstract class Request {

  @Transient
  private ActionCode actionCode;
    
  public Request(ActionCode actionCode) {
    this.actionCode = actionCode;
  }
  
  public ActionCode getAction() {
    return this.actionCode;
  }
  
  public void setAction(ActionCode actionCode) {
    this.actionCode = actionCode;
  }
  
}

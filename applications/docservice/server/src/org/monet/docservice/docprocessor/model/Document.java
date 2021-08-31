package org.monet.docservice.docprocessor.model;

import org.monet.docservice.core.Key;

public class Document {

  public static final int STATE_EDITABLE = 1;
  public static final int STATE_CONSOLIDATED = 2;
  public static final int STATE_LOCKED = 3;
  public static final int STATE_PROCESSING = 4;
  public static final int STATE_ERROR = 5;
  public static final int STATE_OVERWRITTEN = 6;

  private Key key;
  private Template template;
  private int state;
  private boolean isDeprecated;
  
  public void setKey(Key key) {
    this.key = key;
  }
  public Key getKey() {
    return key;
  }
  public void setTemplate(Template template) {
    this.template = template;
  }
  public Template getTemplate() {
    return template;
  }
  public void setState(int state) {
    this.state = state;
  }
  public int getState() {
    return state;
  }
  public void setDeprecated(boolean isDeprecated) {
    this.isDeprecated = isDeprecated;
  }
  public boolean isDeprecated() {
    return isDeprecated;
  }
 
}

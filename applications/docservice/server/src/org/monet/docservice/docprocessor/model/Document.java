package org.monet.docservice.docprocessor.model;

public class Document {

  public static final int STATE_EDITABLE = 1;
  public static final int STATE_CONSOLIDATED = 2;
  public static final int STATE_LOCKED = 3;
  public static final int STATE_PROCESSING = 4;
  public static final int STATE_ERROR = 5;
  public static final int STATE_OVERWRITTEN = 6;

  private String id;
  private Template template;
  private int state;
  private boolean isDeprecated;
  
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return id;
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

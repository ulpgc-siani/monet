package org.monet.metamodel;

import java.util.HashMap;
import java.util.Map;

public abstract class Definition extends DefinitionBase {

  private String fileName;

  public String getLabelString() {
    return null;
  }

  public String getDescription() {
    return null;
  }

  public Map<String, String> getLabelsMap() {
    // TODO: Cambiar!!
    return new HashMap<String, String>();
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getRelativeFileName() {
    return null;
  }

  public String getAbsoluteFileName() {
    return null;
  }

  public void commit() {

  }

}

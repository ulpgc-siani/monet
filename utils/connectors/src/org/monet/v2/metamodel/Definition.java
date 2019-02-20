package org.monet.v2.metamodel;


import net.sf.json.JSONObject;

import java.util.Map;

public abstract class Definition extends DefinitionBase {

  private String fileName;

  public String getLabel() {
    return "";
  }

  public String getDescription() {
    return "";
  }

  public Map<String, String> getLabelsMap() {
    return this._labelMap;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getRelativeFileName() {
    return "";
  }

  public String getAbsoluteFileName() {
    return "";
  }

  public JSONObject serializeToJSON() {
    JSONObject object = new JSONObject();

    object.put("code", this.getCode());
    object.put("label", this.getLabel());

    return object;
  }

}

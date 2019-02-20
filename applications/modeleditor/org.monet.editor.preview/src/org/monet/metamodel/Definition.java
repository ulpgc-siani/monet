package org.monet.metamodel;

import java.util.HashMap;
import java.util.Map;

import org.monet.editor.preview.model.Language;

public abstract class Definition extends DefinitionBase {
  private String fileName;

  public String getLabelString(String language) {
    return Language.getInstance().getModelResource(this._label, language);
  }

  public String getDescription(String language) {
    return Language.getInstance().getModelResource(this._description, language);
  }

  public String getDescriptionString(String language) {
    return Language.getInstance().getModelResource(this._description, language);
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

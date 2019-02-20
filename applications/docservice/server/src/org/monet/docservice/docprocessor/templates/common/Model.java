package org.monet.docservice.docprocessor.templates.common;

import java.util.Collection;

public interface Model {
  String getPropertyAsString(String key);
  Collection<Model> getPropertyAsCollection(String key);
  
  boolean isPropertyAString(String key);
  boolean isPropertyACollection(String key);
  
  void setProperty(String sCode, String text);
  void createCollection(String sCode);
  
  boolean isEmpty();
  
}

package org.monet.docservice.docprocessor.templates.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.monet.docservice.docprocessor.templates.common.Model;

public class ModelImpl implements Model {

  private HashMap<String, Collection<Model>> collectionProperties = new HashMap<String, Collection<Model>>();
  private HashMap<String, String> stringProperties = new HashMap<String, String>();
  
  public Collection<Model> getPropertyAsCollection(String key) {
    return collectionProperties.containsKey(key) ? collectionProperties.get(key) : new ArrayList<Model>();
  }

  public String getPropertyAsString(String key) {
    String prop = stringProperties.get(key);
    if(prop != null)
      prop = prop.trim();
    return prop;
  }

  public boolean isPropertyACollection(String key) {
    return true; //Always true, if a collection doesn't exists, we return an empty one
  }

  public boolean isPropertyAString(String key) {
    return stringProperties.containsKey(key);
  }

  public void setProperty(String key, String value) {
    stringProperties.put(key, value);
  }
  
  public void createCollection(String key) {
    collectionProperties.put(key, new ArrayList<Model>()); 
  }

  public boolean isEmpty() {
    return collectionProperties.size() == 0 && stringProperties.size() == 0;
  }
    
}

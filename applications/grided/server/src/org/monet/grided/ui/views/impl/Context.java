
package org.monet.grided.ui.views.impl;

import java.util.HashMap;
import java.util.Set;

public class Context {
  private String                  sValueForNull;
  private HashMap<String, Object> hmContext;

  public Context() {
    this.sValueForNull = "";
    this.hmContext = new HashMap<String, Object>();
  }

  public void setValueForNull(String sValue) {
    this.sValueForNull = sValue;
  }
  
  public HashMap<String, Object> get() {
    return this.hmContext;
  }

  public Set<String> getKeys() {
    return this.hmContext.keySet();
  }

  public Object get(String sKey) {
    return this.hmContext.get(sKey);
  }

  public boolean containsKey(String sKey) {
    return this.hmContext.containsKey(sKey);
  }

  public Boolean put(String sKey, Object oObject) {
    if (oObject == null) oObject = this.sValueForNull;
    this.hmContext.put(sKey, oObject);
    return true;
  }

  public Boolean remove(String sKey) {
    this.hmContext.remove(sKey);
    return true;
  }

  public Boolean clear() {
    this.hmContext.clear();
    return true;
  }

}
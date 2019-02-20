package org.monet.metamodel.interfaces;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Language {

  protected HashMap<Integer, String> labelMap = new HashMap<Integer, String>();
  
  public String get(int labelId) {
    return this.labelMap.get(labelId);
  }
  
  public void init(String localeCode) {
    
  }
  
  public void merge(Language language) {
    for(Entry<Integer, String> entry : this.labelMap.entrySet()) {
      this.labelMap.put(entry.getKey(), entry.getValue());
    }
  }
  
}

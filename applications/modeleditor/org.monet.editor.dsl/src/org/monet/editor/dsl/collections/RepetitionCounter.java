package org.monet.editor.dsl.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class RepetitionCounter<K, T> {
  
  private HashMap<K, List<T>> map = new HashMap<K, List<T>>();
  
  public void add(K key, T item) {
    List<T> items = this.map.get(key);
    if(items == null) {
      items = new ArrayList<T>();
      this.map.put(key, items);
    }
    items.add(item);
  }
    
  public HashMap<K, T> getNotRepeated() {
    HashMap<K, T> notRepeated = new HashMap<K, T>();
    for(Entry<K, List<T>> entry : this.map.entrySet()) {
      if(entry.getValue().size() == 1)
        notRepeated.put(entry.getKey(), entry.getValue().get(0));
    }
    return notRepeated;
  }
  
  public List<T> getRepeated() {
    List<T> repeated = new ArrayList<T>();
    for(Entry<K, List<T>> entry : this.map.entrySet()) {
      if(entry.getValue().size() > 1)
        repeated.addAll(entry.getValue());
    }
    return repeated;
  }
  
  public void clear() {
    map.clear();
  }

}

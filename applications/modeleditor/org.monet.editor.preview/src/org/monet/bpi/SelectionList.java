package org.monet.bpi;

import java.util.List;

public interface SelectionList {
  
  public void addItem(String key, String value);
  
  public List<Selection> getItems();
  
  public int getTotalCount();
  
}

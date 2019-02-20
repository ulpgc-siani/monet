package org.monet.bpi;

import java.util.List;

public interface FilterList {
  
  public void addItem(String key, String value);
  
  public List<Filter> getItems();
  
  public int getTotalCount();

}
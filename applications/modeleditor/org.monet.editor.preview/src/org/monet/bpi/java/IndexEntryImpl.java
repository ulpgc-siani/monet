package org.monet.bpi.java;

import org.monet.bpi.IndexEntry;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;

public class IndexEntryImpl implements IndexEntry { 
  
  protected Object getAttribute(String name) {
    return null;
  }
  
  protected void setAttribute(String name, Object value) {
  }
  
  public Node getIndexedNode() {
    return null;
  }

  @Override
  public void save() {
    
  }

  @Override
  public Link toLink() {
    return null;
  }

}

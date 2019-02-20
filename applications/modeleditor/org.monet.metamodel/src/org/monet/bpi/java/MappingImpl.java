package org.monet.bpi.java;

import org.monet.bpi.IndexEntry;
import org.monet.bpi.Mapping;
import org.monet.bpi.Node;

public abstract class MappingImpl implements Mapping {

  public IndexEntry genericGetReference() {
    return null;
  }
  
  public Node genericGetNode() {
    return null;
  }
  
  @Override
  public void calculateMapping() {
  }
  
}

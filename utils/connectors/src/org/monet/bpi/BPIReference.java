package org.monet.bpi;

import org.monet.bpi.types.Link;

public interface BPIReference {

  public void save();
  public Link toLink();
  
}
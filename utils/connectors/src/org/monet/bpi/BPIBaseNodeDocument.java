package org.monet.bpi;


public interface BPIBaseNodeDocument<Schema extends BPISchema> 
  extends BPIBaseNode<Schema> {
  
  public void consolidate();
  
}

package org.monet.bpi;


public interface BPIBaseNodeCollection<Schema extends BPISchema> 
  extends BPIBaseNode<Schema> {
  
  public <T extends BPINode<?,?>> void remove(T node);
  
}

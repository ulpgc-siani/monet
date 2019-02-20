package org.monet.bpi;


public interface BPINodeForm<Owner extends BPIBaseNode<?>, 
                             Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeForm<Schema> {
 
  public <T, F extends BPIField<V>, V> T getField(String definition, String name);
  
}

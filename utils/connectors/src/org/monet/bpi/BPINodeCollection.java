package org.monet.bpi;


public interface BPINodeCollection<Owner extends BPIBaseNode<?>, 
                                   Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeCollection<Schema> {
    
}

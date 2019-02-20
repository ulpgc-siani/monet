package org.monet.bpi;


public interface BPINodeContainer<Owner extends BPIBaseNode<?>, 
                                  Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeContainer<Schema> {
    
}

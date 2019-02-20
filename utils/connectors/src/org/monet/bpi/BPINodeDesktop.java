package org.monet.bpi;


public interface BPINodeDesktop<Owner extends BPIBaseNode<?>, 
                                  Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeDesktop<Schema> {
    
}

package org.monet.bpi;


public interface BPINodeCatalog<Owner extends BPIBaseNode<?>, 
                                   Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeCatalog<Schema> {
    
}

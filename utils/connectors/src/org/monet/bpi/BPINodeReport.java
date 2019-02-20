package org.monet.bpi;


public interface BPINodeReport<Owner extends BPIBaseNode<?>, 
                                   Schema extends BPISchema> 
  extends BPINode<Owner, Schema>, BPIBaseNodeCatalog<Schema> {
    
}

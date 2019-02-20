package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class CollectionDefinitionSyncDDBB extends SetDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      CollectionDefinition metaitem = (CollectionDefinition) definition;

  

for(CollectionDefinition.Add item : metaitem.getAddList()) {
}
  }
}



















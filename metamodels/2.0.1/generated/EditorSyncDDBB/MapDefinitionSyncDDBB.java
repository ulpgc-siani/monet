package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class MapDefinitionSyncDDBB extends EntityDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      MapDefinition metaitem = (MapDefinition) definition;

  

for(MapDefinition.Contain item : metaitem.getContainList()) {
}
  }
}



















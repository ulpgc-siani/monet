package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ServiceProviderDefinitionSyncDDBB extends ProviderDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      ServiceProviderDefinition metaitem = (ServiceProviderDefinition) definition;

  

for(ServiceProviderDefinition.Implements item : metaitem.getImplementsList()) {
}
  }
}



















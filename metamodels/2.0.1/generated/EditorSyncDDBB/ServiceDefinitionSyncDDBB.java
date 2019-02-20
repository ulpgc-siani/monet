package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ServiceDefinitionSyncDDBB extends InteroperabilityDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      ServiceDefinition metaitem = (ServiceDefinition) definition;

  

for(ServiceDefinition.Implements item : metaitem.getImplementsList()) {
}
  }
}



















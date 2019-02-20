package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ContainerDefinitionSyncDDBB extends NodeDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      ContainerDefinition metaitem = (ContainerDefinition) definition;

  

for(ContainerDefinition.Contain item : metaitem.getContainList()) {
}
            ContainerViewDeclarationSyncDDBB containerViewDeclaration = new ContainerViewDeclarationSyncDDBB();
      containerViewDeclaration.setModule(this.getModule());
      for(ContainerViewDeclaration include : metaitem.getContainerViewDeclarationList()) {
        containerViewDeclaration.sync(include);
      }
        }
}



















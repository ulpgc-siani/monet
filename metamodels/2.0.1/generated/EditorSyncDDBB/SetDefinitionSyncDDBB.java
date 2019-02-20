package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class SetDefinitionSyncDDBB extends NodeDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      SetDefinition metaitem = (SetDefinition) definition;

  

            SetViewDeclarationSyncDDBB setViewDeclaration = new SetViewDeclarationSyncDDBB();
      setViewDeclaration.setModule(this.getModule());
      for(SetViewDeclaration include : metaitem.getSetViewDeclarationList()) {
        setViewDeclaration.sync(include);
      }
        }
}



















package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class CatalogDefinitionSyncDDBB extends SetDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      CatalogDefinition metaitem = (CatalogDefinition) definition;

  

            ParameterDeclarationSyncDDBB parameterDeclaration = new ParameterDeclarationSyncDDBB();
      parameterDeclaration.setModule(this.getModule());
      for(ParameterDeclaration include : metaitem.getParameterDeclarationList()) {
        parameterDeclaration.sync(include);
      }
        }
}



















package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ReferenceDefinitionSyncDDBB extends DefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      ReferenceDefinition metaitem = (ReferenceDefinition) definition;

  

            AttributeDeclarationSyncDDBB attributeDeclaration = new AttributeDeclarationSyncDDBB();
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.sync(include);
      }
                  ReferenceViewDeclarationSyncDDBB referenceViewDeclaration = new ReferenceViewDeclarationSyncDDBB();
      referenceViewDeclaration.setModule(this.getModule());
      for(ReferenceViewDeclaration include : metaitem.getReferenceViewDeclarationList()) {
        referenceViewDeclaration.sync(include);
      }
        }
}



















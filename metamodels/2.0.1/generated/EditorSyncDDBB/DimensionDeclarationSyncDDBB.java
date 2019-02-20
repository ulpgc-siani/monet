package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DimensionDeclarationSyncDDBB extends IndexedDeclarationSyncDDBB {

  public void sync(DimensionDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            AttributeDeclarationSyncDDBB attributeDeclaration = new AttributeDeclarationSyncDDBB();
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.sync(include);
      }
        }
}



















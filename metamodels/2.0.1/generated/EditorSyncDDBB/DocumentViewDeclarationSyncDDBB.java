package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DocumentViewDeclarationSyncDDBB extends NodeViewDeclarationSyncDDBB {

  public void sync(DocumentViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(DocumentViewDeclaration.Use item : metaitem.getUseList()) {
}
  }
}



















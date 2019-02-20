package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class FormViewDeclarationSyncDDBB extends NodeViewDeclarationSyncDDBB {

  public void sync(FormViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(FormViewDeclaration.Show item : metaitem.getShowList()) {
}
  }
}



















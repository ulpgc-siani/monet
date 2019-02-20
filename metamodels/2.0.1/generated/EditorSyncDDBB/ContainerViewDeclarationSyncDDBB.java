package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ContainerViewDeclarationSyncDDBB extends NodeViewDeclarationSyncDDBB {

  public void sync(ContainerViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(ContainerViewDeclaration.Show item : metaitem.getShowList()) {
}
  }
}



















package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DesktopViewDeclarationSyncDDBB extends NodeViewDeclarationSyncDDBB {

  public void sync(DesktopViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(DesktopViewDeclaration.Show item : metaitem.getShowList()) {
}
  }
}



















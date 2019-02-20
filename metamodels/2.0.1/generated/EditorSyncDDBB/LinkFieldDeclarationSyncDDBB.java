package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class LinkFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(LinkFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(LinkFieldDeclaration.Bind item : metaitem.getBindList()) {
}
  }
}



















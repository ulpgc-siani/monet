package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class SetViewDeclarationSyncDDBB extends NodeViewDeclarationSyncDDBB {

  public void sync(SetViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(SetViewDeclaration.ShowSummary item : metaitem.getShowSummaryList()) {
}
  }
}



















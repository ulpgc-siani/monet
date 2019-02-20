package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ViewDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(ViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

    	if(metaitem.getName() != null)
    this.insertName(metaitem.getName(), metaitem.getCode(), metaitem.getClass());
  }
}



















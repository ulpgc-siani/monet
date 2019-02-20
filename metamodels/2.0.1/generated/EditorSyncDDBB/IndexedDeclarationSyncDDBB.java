package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class IndexedDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(IndexedDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

    this.insertName(metaitem.getName(), metaitem.getCode(), metaitem.getClass());
  }
}



















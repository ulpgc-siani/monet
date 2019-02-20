package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class GenericDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(GenericDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

    this.insertName(metaitem.getName(), metaitem.getCode(), metaitem.getClass());
  }
}



















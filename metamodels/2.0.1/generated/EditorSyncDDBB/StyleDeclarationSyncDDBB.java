package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class StyleDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(StyleDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(StyleDeclaration.When item : metaitem.getWhenList()) {
}
  }
}



















package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class FieldDeclarationSyncDDBB extends IndexedDeclarationSyncDDBB {

  public void sync(FieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(FieldDeclaration.DefaultValue item : metaitem.getDefaultValueList()) {
}
for(FieldDeclaration.Message item : metaitem.getMessageList()) {
}
  }
}



















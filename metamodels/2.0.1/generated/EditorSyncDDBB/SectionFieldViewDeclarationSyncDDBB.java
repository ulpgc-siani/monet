package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class SectionFieldViewDeclarationSyncDDBB extends FieldViewDeclarationSyncDDBB {

  public void sync(SectionFieldViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(SectionFieldViewDeclaration.Column item : metaitem.getColumnList()) {
}
for(SectionFieldViewDeclaration.Show item : metaitem.getShowList()) {
}
  }
}



















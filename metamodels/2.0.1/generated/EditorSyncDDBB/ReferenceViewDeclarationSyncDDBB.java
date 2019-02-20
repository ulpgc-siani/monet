package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ReferenceViewDeclarationSyncDDBB extends ViewDeclarationSyncDDBB {

  public void sync(ReferenceViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(ReferenceViewDeclaration.Show item : metaitem.getShowList()) {
}
            StyleDeclarationSyncDDBB styleDeclaration = new StyleDeclarationSyncDDBB();
      styleDeclaration.setModule(this.getModule());
      for(StyleDeclaration include : metaitem.getStyleDeclarationList()) {
        styleDeclaration.sync(include);
      }
        }
}



















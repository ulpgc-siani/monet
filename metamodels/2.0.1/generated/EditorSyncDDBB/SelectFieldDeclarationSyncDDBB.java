package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class SelectFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(SelectFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            TermIndexDeclarationSyncDDBB termIndexDeclaration = new TermIndexDeclarationSyncDDBB();
      termIndexDeclaration.setModule(this.getModule());
      termIndexDeclaration.sync(metaitem.getTermIndexDeclaration());
        }
}



















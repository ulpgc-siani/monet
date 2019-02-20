package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DrillDeclarationSyncDDBB extends IndexedDeclarationSyncDDBB {

  public void sync(DrillDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            DrillDeclarationSyncDDBB drillDeclaration = new DrillDeclarationSyncDDBB();
      drillDeclaration.setModule(this.getModule());
      drillDeclaration.sync(metaitem.getDrillDeclaration());
        }
}



















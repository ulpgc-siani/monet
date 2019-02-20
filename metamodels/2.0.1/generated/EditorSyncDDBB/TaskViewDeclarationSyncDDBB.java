package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class TaskViewDeclarationSyncDDBB extends ViewDeclarationSyncDDBB {

  public void sync(TaskViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(TaskViewDeclaration.Show item : metaitem.getShowList()) {
}
  }
}



















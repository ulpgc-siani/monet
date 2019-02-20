package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class WorkmapDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(WorkmapDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            WorkplaceDeclarationSyncDDBB workplaceDeclaration = new WorkplaceDeclarationSyncDDBB();
      workplaceDeclaration.setModule(this.getModule());
      for(WorkplaceDeclaration include : metaitem.getWorkplaceDeclarationList()) {
        workplaceDeclaration.sync(include);
      }
                  WorklineDeclarationSyncDDBB worklineDeclaration = new WorklineDeclarationSyncDDBB();
      worklineDeclaration.setModule(this.getModule());
      for(WorklineDeclaration include : metaitem.getWorklineDeclarationList()) {
        worklineDeclaration.sync(include);
      }
        }
}



















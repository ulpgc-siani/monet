package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ModelSyncDDBB extends DeclarationSync {

  public void sync(Model metaitem) throws Exception {
  if(metaitem == null) return;
  
  

    this.insertName(metaitem.getName(), metaitem.getCode(), metaitem.getClass());
            GenericDeclarationSyncDDBB genericDeclaration = new GenericDeclarationSyncDDBB();
      genericDeclaration.setModule(this.getModule());
      for(GenericDeclaration include : metaitem.getGenericDeclarationList()) {
        genericDeclaration.sync(include);
      }
                  ResolveDeclarationSyncDDBB resolveDeclaration = new ResolveDeclarationSyncDDBB();
      resolveDeclaration.setModule(this.getModule());
      for(ResolveDeclaration include : metaitem.getResolveDeclarationList()) {
        resolveDeclaration.sync(include);
      }
        }
}



















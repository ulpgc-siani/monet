package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DefinitionSyncDDBB extends DefinitionSync {

  public void sync(Definition definition) throws Exception {
      Definition metaitem = (Definition) definition;

  

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



















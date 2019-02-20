package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DesktopDefinitionSyncDDBB extends NodeDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      DesktopDefinition metaitem = (DesktopDefinition) definition;

  

            DesktopViewDeclarationSyncDDBB desktopViewDeclaration = new DesktopViewDeclarationSyncDDBB();
      desktopViewDeclaration.setModule(this.getModule());
      for(DesktopViewDeclaration include : metaitem.getDesktopViewDeclarationList()) {
        desktopViewDeclaration.sync(include);
      }
        }
}



















package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class TermDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(TermDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            TermDeclarationSyncDDBB termDeclaration = new TermDeclarationSyncDDBB();
      termDeclaration.setModule(this.getModule());
      for(TermDeclaration include : metaitem.getTermDeclarationList()) {
        termDeclaration.sync(include);
      }
        }
}



















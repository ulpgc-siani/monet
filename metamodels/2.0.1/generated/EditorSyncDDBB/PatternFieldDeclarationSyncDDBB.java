package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class PatternFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(PatternFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            PatternDeclarationSyncDDBB patternDeclaration = new PatternDeclarationSyncDDBB();
      patternDeclaration.setModule(this.getModule());
      for(PatternDeclaration include : metaitem.getPatternDeclarationList()) {
        patternDeclaration.sync(include);
      }
        }
}



















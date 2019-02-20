package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class IndicatorDeclarationSyncDDBB extends IndexedDeclarationSyncDDBB {

  public void sync(IndicatorDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            FormulaDeclarationSyncDDBB formulaDeclaration = new FormulaDeclarationSyncDDBB();
      formulaDeclaration.setModule(this.getModule());
      formulaDeclaration.sync(metaitem.getFormulaDeclaration());
        }
}



















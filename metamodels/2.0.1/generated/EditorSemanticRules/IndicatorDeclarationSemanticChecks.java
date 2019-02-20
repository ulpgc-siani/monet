package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class IndicatorDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(IndicatorDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                      
            
            if(metaitem.getData() != null) {
      IndicatorDeclaration.Data item = metaitem.getData();
                    this.checkName(item.getAttribute(), AttributeDeclaration.class);
                          }
                  FormulaDeclarationSemanticChecks formulaDeclaration = new FormulaDeclarationSemanticChecks();
      formulaDeclaration.setProblems(this.getProblems());
      formulaDeclaration.setModule(this.getModule());
      formulaDeclaration.check(metaitem.getFormulaDeclaration());
        }
}



















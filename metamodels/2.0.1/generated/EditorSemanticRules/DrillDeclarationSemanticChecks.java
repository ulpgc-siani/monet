package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DrillDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(DrillDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getUse() != null) {
      DrillDeclaration.Use item = metaitem.getUse();
                    if(item.getDimension() != null && !item.getDimension().isEmpty())
      this.checkName(item.getDimension(), DimensionDeclaration.class);
            }
                  DrillDeclarationSemanticChecks drillDeclaration = new DrillDeclarationSemanticChecks();
      drillDeclaration.setProblems(this.getProblems());
      drillDeclaration.setModule(this.getModule());
      drillDeclaration.check(metaitem.getDrillDeclaration());
        }
}



















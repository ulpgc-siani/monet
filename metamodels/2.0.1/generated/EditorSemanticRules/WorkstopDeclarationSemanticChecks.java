package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class WorkstopDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(WorkstopDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      this.checkName(metaitem.getWorkplace(), WorkplaceDeclaration.class);
    
                    }
}



















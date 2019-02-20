package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class MetricDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(MetricDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                      
            
            if(metaitem.getEquivalence() != null) {
      MetricDeclaration.Equivalence item = metaitem.getEquivalence();
                          }
        }
}



















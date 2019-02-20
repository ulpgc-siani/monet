package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class NumberFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(NumberFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getFormat() != null) {
      NumberFieldDeclaration.Format item = metaitem.getFormat();
                          }
          
            if(metaitem.getRange() != null) {
      NumberFieldDeclaration.Range item = metaitem.getRange();
                                        }
                  MetricDeclarationSemanticChecks metricDeclaration = new MetricDeclarationSemanticChecks();
      metricDeclaration.setProblems(this.getProblems());
      metricDeclaration.setModule(this.getModule());
      for(MetricDeclaration include : metaitem.getMetricDeclarationList()) {
        metricDeclaration.check(include);
      }
        }
}



















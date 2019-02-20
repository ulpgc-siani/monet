package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class AttributeDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(AttributeDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      if(metaitem.getDimension() != null && !metaitem.getDimension().isEmpty())
      this.checkName(metaitem.getDimension(), DimensionDeclaration.class);
    
                      
                      
          }
}



















package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DateFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(DateFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getFormat() != null) {
      DateFieldDeclaration.Format item = metaitem.getFormat();
                          }
        }
}



















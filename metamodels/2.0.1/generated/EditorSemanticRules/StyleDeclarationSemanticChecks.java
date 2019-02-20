package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class StyleDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(StyleDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                    for(StyleDeclaration.When item : metaitem.getWhenList()) {
                        this.checkName(item.getAttribute(), AttributeDeclaration.class);
                                  }
              }
}



















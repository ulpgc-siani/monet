package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class FormViewDeclarationSemanticChecks extends NodeViewDeclarationSemanticChecks {

  public void check(FormViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                    for(FormViewDeclaration.Show item : metaitem.getShowList()) {
                        this.checkName(item.getField(), FieldDeclaration.class);
                }
              }
}



















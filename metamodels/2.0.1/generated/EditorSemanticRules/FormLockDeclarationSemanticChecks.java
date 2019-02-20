package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class FormLockDeclarationSemanticChecks extends WorklockDeclarationSemanticChecks {

  public void check(FormLockDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getUse() != null) {
      FormLockDeclaration.Use item = metaitem.getUse();
                    this.checkName(item.getForm(), FormDefinition.class);
                    if(item.getView() != null && !item.getView().isEmpty())
      this.checkName(item.getView(), FormViewDeclaration.class);
            }
        }
}



















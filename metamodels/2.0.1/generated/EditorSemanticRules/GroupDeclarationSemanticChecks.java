package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class GroupDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(GroupDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      this.checkName(metaitem.getAttribute(), AttributeDeclaration.class);
  }
}


















